package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.admin.TrackFormDto;
import it.webapp.ac_community_ita.service.admin.AdminTrackService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tracks")
public class AdminTrackController {

    private final AdminTrackService adminTrackService;

    public AdminTrackController(AdminTrackService adminTrackService) {
        this.adminTrackService = adminTrackService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tracks", adminTrackService.findAll());
        return "admin/tracks-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("trackDto", new TrackFormDto());
        return "admin/track-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute TrackFormDto trackFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/track-form";
        }
        try {
            adminTrackService.create(trackFormDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "admin/track-form";
        }
        return "redirect:/admin/tracks";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminTrackService.delete(id);
        return "redirect:/admin/tracks";
    }
}