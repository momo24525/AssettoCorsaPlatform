package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.config.UserPrincipal;
import it.webapp.ac_community_ita.entity.User;
import it.webapp.ac_community_ita.service.EventService;
import it.webapp.ac_community_ita.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserService userService;

    public HomeController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        if (userPrincipal != null) {
            String steamId = userPrincipal.getUser().getSteamId();
            User user = userService.findbySteamId(steamId); // <-- utente fresco dal DB

            model.addAttribute("showUpdateProfile", !user.isCompleted());
        } else {
            model.addAttribute("showUpdateProfile", false);
        }
        eventService.getNextEvent()
                .ifPresent(dto -> model.addAttribute("nextEvent", dto));
        model.addAttribute("scheduledEvents", eventService.getUpcomingEvents(6));
        return "home";
    }
}