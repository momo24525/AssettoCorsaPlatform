package it.webapp.ac_community_ita.service.events;

import it.webapp.ac_community_ita.dto.events.EventSummaryDto;
import it.webapp.ac_community_ita.entity.event.Event;
import it.webapp.ac_community_ita.entity.event.EventStatus;
import it.webapp.ac_community_ita.entity.registration.RegistrationStatus;
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

    public Optional<EventSummaryDto> getLiveEvent(Long viewerId) {
        return eventRepository.findFirstByStatusOrderByStartTimeAsc(EventStatus.LIVE)
                .map(event -> toSummaryDto(event, viewerId));
    }

    public List<EventSummaryDto> getUpcomingEvents(int limit, Long viewerId) {
        return eventRepository.findUpcomingEvents(LocalDateTime.now(), EventStatus.SCHEDULED, PageRequest.of(0, limit))
                .stream()
                .map(event -> toSummaryDto(event, viewerId))
                .collect(Collectors.toList());
    }

    public List<EventSummaryDto> getAllUpcomingEvents(Long viewerId) {
        return eventRepository.findUpcomingEvents(LocalDateTime.now(), EventStatus.SCHEDULED, Pageable.unpaged())
                .stream()
                .map(event -> toSummaryDto(event, viewerId))
                .collect(Collectors.toList());
    }

    public Optional<EventSummaryDto> getNextScheduledEvent(Long viewerId) {
        return eventRepository
                .findUpcomingEvents(LocalDateTime.now(), EventStatus.SCHEDULED, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(event -> toSummaryDto(event, viewerId));
    }

    private EventSummaryDto toSummaryDto(Event event, Long viewerId) {
        long registeredCount = registrationRepository.countByEventIdAndStatus(
                event.getId(),
                RegistrationStatus.CONFIRMED
        );

        boolean registered = viewerId != null
                && registrationRepository.findByUserIdAndEventId(viewerId, event.getId())
                .map(r -> r.getStatus() != RegistrationStatus.CANCELLED)
                .orElse(false);

        return new EventSummaryDto(
                event.getId(),
                event.getTitle(),
                event.getTrack().getName(),
                event.getCar().getName(),
                event.getStartTime(),
                (int) registeredCount,
                event.getMaxPlayers(),
                event.getTrack().getUrlImage(),
                registered,
                event.getStatus()
        );
    }
}