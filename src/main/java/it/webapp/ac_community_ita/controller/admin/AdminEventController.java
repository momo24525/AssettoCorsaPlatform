package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.admin.EventFormDto;
import it.webapp.ac_community_ita.service.admin.AdminCarService;
import it.webapp.ac_community_ita.service.admin.AdminEventService;
import it.webapp.ac_community_ita.service.admin.AdminTrackService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/event")

public class AdminEventController {

    private final AdminEventService adminEventService;
    private final AdminCarService adminCarService;
    private final AdminTrackService adminTrackService;

    public AdminEventController(AdminEventService adminEventService, AdminCarService adminCarService, AdminTrackService adminTrackService) {
        this.adminCarService = adminCarService;
        this.adminEventService = adminEventService;
        this.adminTrackService = adminTrackService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("event", adminEventService.findAll());
        return "admin/event-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("eventDto", new EventFormDto());
        model.addAttribute("cars", adminCarService.findAll());
        model.addAttribute("tracks", adminTrackService.findAll());
        return "admin/event-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute EventFormDto eventFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/event-form";
        }
        adminEventService.create(eventFormDto);
        return "redirect:/admin/event";
    }



    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminEventService.delete(id);
        return "redirect:/admin/event";
    }
}