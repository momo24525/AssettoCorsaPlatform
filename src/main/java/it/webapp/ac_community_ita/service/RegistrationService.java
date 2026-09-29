package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.EventSummaryDto;
import it.webapp.ac_community_ita.entity.Event;
import it.webapp.ac_community_ita.entity.Registration;
import it.webapp.ac_community_ita.entity.RegistrationStatus;
import it.webapp.ac_community_ita.entity.User;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.RegistrationRepository;
import it.webapp.ac_community_ita.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public RegistrationStatus register(Long userId, Long eventId){

         Event event = eventRepository.findById(eventId)
                 .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));


        if (event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("L'evento è già iniziato");
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
        }
        else {
            registration = existing.get();
        }

        Long subscribed = registrationRepository.countByEventIdAndStatus(eventId, RegistrationStatus.CONFIRMED);

        if (subscribed < event.getMaxPlayers()){
            registration.setStatus(RegistrationStatus.CONFIRMED);
        }
        else {registration.setStatus(RegistrationStatus.WAITLIST);}
        registration.setRegistrationDate(LocalDateTime.now());
        registrationRepository.save(registration);

        return registration.getStatus();
    }

    @Transactional
    public void cancel(Long userId, Long eventId) {
        // 1. trova la registrazione con findByUserIdAndEventId
        Optional<Registration> existing = registrationRepository.findByUserIdAndEventId(userId, eventId);
        // 2. se non esiste, o è già CANCELLED, lancia un'eccezione
        if (existing.isEmpty() || existing.get().getStatus() == RegistrationStatus.CANCELLED)
        {throw new IllegalStateException("Registrazione inesistente o già cancellata");}

        // 3. altrimenti, imposta lo stato a CANCELLED e salva
        Registration registration = existing.get();
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }
}


