package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.config.UserPrincipal;
import it.webapp.ac_community_ita.repository.EventRepository;
import it.webapp.ac_community_ita.repository.RegistrationRepository;
import it.webapp.ac_community_ita.repository.UserRepository;
import it.webapp.ac_community_ita.service.AdminCarService;
import it.webapp.ac_community_ita.service.AdminEventService;
import it.webapp.ac_community_ita.service.AdminTrackService;
import it.webapp.ac_community_ita.service.RegistrationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    // il service nel costruttore, come al solito
    private final RegistrationService registrationService;


    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping("/events/{eventId}/register")
    public String register(@PathVariable Long eventId,
                           @AuthenticationPrincipal UserPrincipal userPrincipal) {

        // 1. prendi l'id dell'utente
        Long userId = userPrincipal.getUser().getId();
        // 2. chiama registrationService.register(userId, eventId)
        registrationService.register(userId, eventId);
        // 3. redirect
        return "redirect:/home";
    }
}