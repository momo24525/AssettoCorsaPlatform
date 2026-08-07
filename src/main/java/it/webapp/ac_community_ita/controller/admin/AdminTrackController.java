package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.TrackDto;
import it.webapp.ac_community_ita.service.AdminTrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tracks")
@RequiredArgsConstructor
public class AdminTrackController {

    private final AdminTrackService adminTrackService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tracks", adminTrackService.findAll());
        return "admin/tracks-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("trackDto", new TrackDto());
        return "admin/track-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute TrackDto trackDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/track-form";
        }
        try {
            adminTrackService.create(trackDto);
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