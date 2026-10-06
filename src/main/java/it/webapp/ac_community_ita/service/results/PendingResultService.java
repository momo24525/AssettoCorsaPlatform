package it.webapp.ac_community_ita.service.results;

import it.webapp.ac_community_ita.dto.results.AcResultJsonDto;
import it.webapp.ac_community_ita.entity.results.PendingResult;
import it.webapp.ac_community_ita.repository.PendingResultRepository;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
public class PendingResultService {

    private final PendingResultRepository pendingResultRepository;
    private final ObjectMapper objectMapper;

    public PendingResultService(PendingResultRepository pendingResultRepository, ObjectMapper objectMapper) {
        this.pendingResultRepository = pendingResultRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Legge il JSON grezzo e, se la sessione e' una RACE, lo salva come PendingResult.
     *
     * @return true se e' stato salvato, false se la sessione non e' una gara
     * @throws IllegalArgumentException se il JSON non e' valido
     */
    public boolean saveIfRace(String filename, String rawJson) {
        // File gia' ricevuto (es. invio ripetuto dopo un timeout): non salvare di nuovo
        if (pendingResultRepository.existsByFilename(filename)) {
            return false;
        }

        AcResultJsonDto dto;
        try {
            dto = objectMapper.readValue(rawJson, AcResultJsonDto.class);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("JSON dei risultati non valido: " + filename, e);
        }

        if (!"RACE".equalsIgnoreCase(dto.getType())) {
            return false;
        }

        PendingResult pending = new PendingResult();
        pending.setFilename(filename);
        pending.setTrackName(dto.getTrackName());
        pending.setRawJson(rawJson);
        pending.setReceivedAt(LocalDateTime.now());

        pendingResultRepository.save(pending);
        return true;
    }
}