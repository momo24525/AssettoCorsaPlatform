package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.EventDto;
import it.webapp.ac_community_ita.service.AdminCarService;
import it.webapp.ac_community_ita.service.AdminEventService;
import it.webapp.ac_community_ita.service.AdminTrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;
    private final AdminCarService adminCarService;
    private final AdminTrackService adminTrackService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("event", adminEventService.findAll());
        return "admin/event-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("eventDto", new EventDto());
        model.addAttribute("cars", adminCarService.findAll());
        model.addAttribute("tracks", adminTrackService.findAll());
        return "admin/event-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute EventDto eventDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/event-form";
        }
        adminEventService.create(eventDto);
        return "redirect:/admin/event";
    }



    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminEventService.delete(id);
        return "redirect:/admin/event";
    }
}