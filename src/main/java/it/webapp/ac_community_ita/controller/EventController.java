package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.webapp.ac_community_ita.config.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String events(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        Long viewerId = userPrincipal != null ? userPrincipal.getUser().getId() : null;
        model.addAttribute("scheduledEvents", eventService.getAllUpcomingEvents(viewerId));
        return "events";
    }
}