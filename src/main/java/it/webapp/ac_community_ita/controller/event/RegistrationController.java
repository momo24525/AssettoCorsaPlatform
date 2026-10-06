package it.webapp.ac_community_ita.controller.event;

import it.webapp.ac_community_ita.security.UserPrincipal;
import it.webapp.ac_community_ita.service.events.RegistrationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {


    private final RegistrationService registrationService;


    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping("/events/{eventId}/register")
    public String register(@PathVariable Long eventId,
                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                           RedirectAttributes redirectAttributes) {

        Long userId = userPrincipal.getUser().getId();
        try {
            registrationService.register(userId, eventId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/home";
    }

    @PostMapping("/events/{eventId}/cancel")
    public String cancel(@PathVariable Long eventId,
                         @AuthenticationPrincipal UserPrincipal userPrincipal,
                         RedirectAttributes redirectAttributes) {
        Long userId = userPrincipal.getUser().getId();
        try {
            registrationService.cancel(userId, eventId);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }
}