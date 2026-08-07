package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.EventDto;
import it.webapp.ac_community_ita.entity.Car;
import it.webapp.ac_community_ita.entity.Event;
import it.webapp.ac_community_ita.entity.EventStatus;
import it.webapp.ac_community_ita.entity.Track;
import it.webapp.ac_community_ita.repository.CarRepository;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;
    private final CarRepository carRepository;
    private final TrackRepository trackRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato con id: " + id));
    }

    public Event create(EventDto dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Vettura non trovata con id: " + dto.getCarId()));

        Track track = trackRepository.findById(dto.getTrackId())
                .orElseThrow(() -> new EntityNotFoundException("Tracciato non trovato con id: " + dto.getTrackId()));

        Event event = new Event();
        event.setCar(car);
        event.setTrack(track);
        event.setTitle(
                (dto.getTitle() != null && !dto.getTitle().isBlank())
                        ? dto.getTitle()
                        : car.getName() + " " + track.getName()
        );
        event.setCreatedAt(LocalDateTime.now());
        event.setMaxPlayers(dto.getMaxPlayers());
        event.setStartTime(dto.getStartTime());
        event.setQualyDuration(dto.getQualyDuration());
        event.setRaceDuration(dto.getRaceDuration());
        event.setStatus(EventStatus.SCHEDULED);
        return eventRepository.save(event);
    }


    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}