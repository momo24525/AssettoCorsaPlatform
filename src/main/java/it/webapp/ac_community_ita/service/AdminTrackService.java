package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.TrackDto;
import it.webapp.ac_community_ita.entity.Track;
import it.webapp.ac_community_ita.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTrackService {

    private final TrackRepository trackRepository;

    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    public Track findById(Long id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track non trovata con id: " + id));
    }

    public Track create(TrackDto dto) {
        if (trackRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Esiste già una pista con il nome" + dto.getName());
        }
        Track track = new Track();
        track.setName(dto.getName());
        return trackRepository.save(track);
    }

    public Track update(Long id, TrackDto dto) {
        Track track = findById(id);
        track.setName(dto.getName());
        return trackRepository.save(track);
    }

    public void delete(Long id) {
        trackRepository.deleteById(id);
    }
}