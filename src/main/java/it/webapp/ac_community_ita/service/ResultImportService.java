package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.results.AcResultJsonDto;
import it.webapp.ac_community_ita.dto.results.LapEntryDto;
import it.webapp.ac_community_ita.dto.results.PendingResultPreviewDto;
import it.webapp.ac_community_ita.dto.results.ResultEntryDto;
import it.webapp.ac_community_ita.entity.*;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.PendingResultRepository;
import it.webapp.ac_community_ita.repository.ResultRepository;
import it.webapp.ac_community_ita.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultImportService {

    // Valore che AC usa quando un pilota non ha giri validi
    private static final int NO_LAP = 999_999_999;

    private final PendingResultRepository pendingResultRepository;
    private final ResultRepository resultRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public ResultImportService(PendingResultRepository pendingResultRepository, ResultRepository resultRepository, EventRepository eventRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.pendingResultRepository = pendingResultRepository;
        this.resultRepository = resultRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    // ---------------------------------------------------------------- lettura

    @Transactional(readOnly = true)
    public List<PendingResult> listPending() {
        return pendingResultRepository.findAllByOrderByReceivedAtAsc();
    }

    /** Cosa verrebbe creato confermando questo PendingResult. */
    @Transactional(readOnly = true)
    public PendingResultPreviewDto preview(Long pendingId) {
        PendingResult pending = getPending(pendingId);
        AcResultJsonDto dto = parse(pending);
        Map<String, Long> lapsByGuid = lapsByGuid(dto);

        List<PendingResultPreviewDto.Row> rows = new ArrayList<>();
        for (DriverEntry d : driverEntries(dto)) {
            ResultEntryDto e = d.entry();
            String username = userRepository.findBySteamId(e.getDriverGuid())
                    .map(User::getUsername)
                    .orElse(null);
            rows.add(new PendingResultPreviewDto.Row(
                    d.position(),
                    e.getDriverName(),
                    e.getDriverGuid(),
                    username,
                    lapsByGuid.getOrDefault(e.getDriverGuid(), 0L).intValue(),
                    formatMs(e.getBestLap()),
                    formatMs(e.getTotalTime())));
        }

        return new PendingResultPreviewDto(
                pending.getId(),
                pending.getFilename(),
                normalizeTrack(pending.getTrackName()),
                pending.getReceivedAt(),
                dto.getRaceLaps(),
                suggestEventId(pending),
                rows);
    }

    // ---------------------------------------------------------------- azioni

    /**
     * Deserializza il JSON grezzo e crea i Result per l'evento scelto.
     *
     * @return quanti Result sono stati creati
     */
    @Transactional
    public int confirm(Long pendingId, Long eventId) {
        PendingResult pending = getPending(pendingId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato: " + eventId));

        if (resultRepository.existsByEventId(eventId)) {
            throw new IllegalStateException("Questo evento ha gia' dei risultati");
        }

        AcResultJsonDto dto = parse(pending);
        Map<String, Long> lapsByGuid = lapsByGuid(dto);

        List<Result> toSave = new ArrayList<>();
        for (DriverEntry d : driverEntries(dto)) {
            ResultEntryDto e = d.entry();

            // Il Guid di AC e' lo SteamID64, che nel DB e' User.steamId
            Optional<User> user = userRepository.findBySteamId(e.getDriverGuid());
            if (user.isEmpty()) {
                continue; // pilota non registrato sul sito: salta (la posizione resta quella reale)
            }

            Result r = new Result();
            r.setEvent(event);
            r.setUser(user.get());
            r.setPosition(d.position());
            r.setLaps(lapsByGuid.getOrDefault(e.getDriverGuid(), 0L).intValue());
            r.setBestLapMs(isValidTime(e.getBestLap()) ? e.getBestLap() : null);
            r.setFinishTimeMs(isValidTime(e.getTotalTime()) ? e.getTotalTime() : null); // 0 = non ha finito
            toSave.add(r);
        }

        resultRepository.saveAll(toSave);
        event.setStatus(EventStatus.FINISHED); // DA VEDERE SE GESTIRLO QUI O ALTROVE
        pendingResultRepository.delete(pending); // oppure aggiungi un flag "processed"
        return toSave.size();
    }

    @Transactional
    public void discard(Long pendingId) {
        pendingResultRepository.deleteById(pendingId);
    }

    // ---------------------------------------------------------------- helper

    private record DriverEntry(int position, ResultEntryDto entry) {
    }

    /** Righe di "Result" con un pilota vero: niente slot vuoti (Guid "") e niente guid doppi. */
    private List<DriverEntry> driverEntries(AcResultJsonDto dto) {
        List<DriverEntry> out = new ArrayList<>();
        if (dto.getResult() == null) {
            return out;
        }
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < dto.getResult().size(); i++) {
            ResultEntryDto e = dto.getResult().get(i);
            String guid = e.getDriverGuid();
            if (guid == null || guid.isBlank() || !seen.add(guid)) {
                continue;
            }
            out.add(new DriverEntry(i + 1, e)); // "Result" e' gia' ordinato per classifica
        }
        return out;
    }

    /** Giri per pilota: in "Laps" c'e' una entry per ogni giro. */
    private Map<String, Long> lapsByGuid(AcResultJsonDto dto) {
        if (dto.getLaps() == null) {
            return Map.of();
        }
        return dto.getLaps().stream()
                .filter(l -> l.getDriverGuid() != null && !l.getDriverGuid().isBlank())
                .collect(Collectors.groupingBy(LapEntryDto::getDriverGuid, Collectors.counting()));
    }

    /** Evento con pista corrispondente e orario piu' vicino alla ricezione del file (null se nessuno). */
    private Long suggestEventId(PendingResult pending) {
        String track = normalizeTrack(pending.getTrackName()).toLowerCase(Locale.ROOT);
        if (track.isBlank()) {
            return null;
        }
        return eventRepository.findAll().stream()
                .filter(e -> e.getTrack().getName().toLowerCase(Locale.ROOT).contains(track))
                .min(Comparator.comparingLong(e ->
                        Math.abs(Duration.between(e.getStartTime(), pending.getReceivedAt()).toMinutes())))
                .map(Event::getId)
                .orElse(null);
    }

    /** "csp/0/../imola" -> "imola" */
    private String normalizeTrack(String trackName) {
        if (trackName == null) {
            return "";
        }
        return trackName.substring(trackName.lastIndexOf('/') + 1);
    }

    private PendingResult getPending(Long id) {
        return pendingResultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Risultato in sospeso non trovato: " + id));
    }

    private AcResultJsonDto parse(PendingResult pending) {
        try {
            return objectMapper.readValue(pending.getRawJson(), AcResultJsonDto.class);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("JSON non valido: " + pending.getFilename(), e);
        }
    }

    private boolean isValidTime(Integer ms) {
        return ms != null && ms > 0 && ms < NO_LAP;
    }

    /** 133560 -> "2:13.560" */
    private String formatMs(Integer ms) {
        if (!isValidTime(ms)) {
            return "-";
        }
        return String.format(Locale.ROOT, "%d:%02d.%03d", ms / 60_000, (ms % 60_000) / 1000, ms % 1000);
    }
}