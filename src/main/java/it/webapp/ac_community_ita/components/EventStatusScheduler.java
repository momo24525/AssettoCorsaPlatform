package it.webapp.ac_community_ita.components;

import it.webapp.ac_community_ita.entity.Event;
import it.webapp.ac_community_ita.entity.EventStatus;
import it.webapp.ac_community_ita.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(EventStatusScheduler.class);

    // Minuti prima dell'inizio in cui l'evento passa a LIVE e le iscrizioni si chiudono
    public static final long LIVE_WINDOW_MINUTES = 10;

    private final EventRepository eventRepository;

    public EventStatusScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void openLiveEvents() {
        LocalDateTime threshold = LocalDateTime.now().plusMinutes(LIVE_WINDOW_MINUTES);

        List<Event> events = eventRepository
                .findByStatusAndStartTimeLessThanEqual(EventStatus.SCHEDULED, threshold);

        for (Event event : events) {
            event.setStatus(EventStatus.LIVE);
            log.info("Evento {} ('{}') passato a LIVE", event.getId(), event.getTitle());
        }
    }
}