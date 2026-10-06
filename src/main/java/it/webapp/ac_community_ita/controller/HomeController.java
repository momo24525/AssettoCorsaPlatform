package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.scheduler.EventStatusScheduler;
import it.webapp.ac_community_ita.security.UserPrincipal;
import it.webapp.ac_community_ita.dto.events.EventSummaryDto;
import it.webapp.ac_community_ita.entity.user.User;
import it.webapp.ac_community_ita.service.events.EventService;
import it.webapp.ac_community_ita.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserService userService;

    public HomeController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        Long viewerId = null;
        if (userPrincipal != null) {
            User user = userPrincipal.getUser();
            viewerId = user.getId();
            model.addAttribute("showUpdateProfile", !user.isCompleted());
        } else {
            model.addAttribute("showUpdateProfile", false);
        }

        Optional<EventSummaryDto> liveEvent = eventService.getLiveEvent(viewerId);
        if (liveEvent.isPresent()) {
            model.addAttribute("liveEvent", liveEvent.get());
        } else {
            eventService.getNextScheduledEvent(viewerId).ifPresent(next -> {
                model.addAttribute("nextEvent", next);
                LocalDateTime liveAt = next.getStartTime()
                        .minusMinutes(EventStatusScheduler.LIVE_WINDOW_MINUTES);
                long seconds = Math.max(0, Duration.between(LocalDateTime.now(), liveAt).getSeconds());

                // il countdown parte solo se mancano meno di 24 ore
                if (seconds <= 24 * 60 * 60) {
                    model.addAttribute("secondsToLive", seconds);
                }
            });
        }

        model.addAttribute("scheduledEvents", eventService.getUpcomingEvents(6, viewerId));
        return "home";
    }
}