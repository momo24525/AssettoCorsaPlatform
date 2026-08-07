package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        eventService.getNextEvent()
                .ifPresent(dto -> model.addAttribute("nextEvent", dto));
        model.addAttribute("scheduledEvents", eventService.getUpcomingEvents(6));
        return "home";
    }
}