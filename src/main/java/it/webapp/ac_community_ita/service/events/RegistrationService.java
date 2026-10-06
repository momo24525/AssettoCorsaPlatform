package it.webapp.ac_community_ita.service.events;

import it.webapp.ac_community_ita.entity.event.Event;
import it.webapp.ac_community_ita.entity.event.EventStatus;
import it.webapp.ac_community_ita.entity.registration.Registration;
import it.webapp.ac_community_ita.entity.registration.RegistrationStatus;
import it.webapp.ac_community_ita.entity.user.User;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.RegistrationRepository;
import it.webapp.ac_community_ita.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService {


    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;


    public RegistrationService(EventRepository eventRepository,
                               RegistrationRepository registrationRepository,
                               UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegistrationStatus register(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));


        if (event.getStatus() != EventStatus.SCHEDULED) {
            throw new IllegalStateException("Le iscrizioni per questo evento sono chiuse");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User non trovato"));

        Optional<Registration> existing = registrationRepository.findByUserIdAndEventId(userId, eventId);

        if (existing.isPresent() && existing.get().getStatus() != RegistrationStatus.CANCELLED) {
            throw new IllegalStateException("Sei già iscritto a questo evento");
        }

        Registration registration;

        if (existing.isEmpty()) {
            registration = new Registration();
            registration.setUser(user);
            registration.setEvent(event);
        } else {
            registration = existing.get();
        }

        Long subscribed = registrationRepository.countByEventIdAndStatus(eventId, RegistrationStatus.CONFIRMED);

        if (subscribed < event.getMaxPlayers()) {
            registration.setStatus(RegistrationStatus.CONFIRMED);
        } else {
            registration.setStatus(RegistrationStatus.WAITLIST);
        }
        registration.setRegistrationDate(LocalDateTime.now());
        registrationRepository.save(registration);

        return registration.getStatus();
    }

    @Transactional
    public void cancel(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));

        if (event.getStatus() != EventStatus.SCHEDULED) {
            throw new IllegalStateException("Non puoi cancellare l'iscrizione: l'evento è già chiuso alle iscrizioni");
        }

        Optional<Registration> existing = registrationRepository.findByUserIdAndEventId(userId, eventId);


        if (existing.isEmpty() || existing.get().getStatus() == RegistrationStatus.CANCELLED) {
            throw new IllegalStateException("Registrazione inesistente o già cancellata");
        }


        Registration registration = existing.get();
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }
}


