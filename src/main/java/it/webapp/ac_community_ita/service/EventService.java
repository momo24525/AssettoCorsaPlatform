package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.EventSummaryDto;
import it.webapp.ac_community_ita.entity.Event;
import it.webapp.ac_community_ita.entity.RegistrationStatus;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.RegistrationRepository;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository,
                        RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public Optional<EventSummaryDto> getNextEvent() {
        List<Event> upcomingEvents = eventRepository.findUpcomingEvents(LocalDateTime.now(), PageRequest.of(0,1));

        return upcomingEvents.stream()
                .findFirst()
                .map(this::toSummaryDto);
    }

    public List<EventSummaryDto> getUpcomingEvents(int limit) {
        return eventRepository.findUpcomingEvents(LocalDateTime.now(), PageRequest.of(0, limit))
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public List<EventSummaryDto> getAllUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDateTime.now(), Pageable.unpaged())
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    private EventSummaryDto toSummaryDto(Event event) {
        long registeredCount = registrationRepository.countByEventIdAndStatus(
                event.getId(),
                RegistrationStatus.CONFIRMED
        );

        return new EventSummaryDto(
                event.getId(),
                event.getTitle(),
                event.getTrack().getName(),
                event.getCar().getName(),
                event.getStartTime(),
                (int) registeredCount,
                event.getMaxPlayers(),
                event.getTrack().getUrlImage()
        );
    }
}