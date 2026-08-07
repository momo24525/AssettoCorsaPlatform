package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("scheduledEvents", eventService.getAllUpcomingEvents());
        return "events";
    }
}