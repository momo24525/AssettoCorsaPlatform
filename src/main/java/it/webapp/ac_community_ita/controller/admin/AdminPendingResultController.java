package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.service.AdminEventService;
import it.webapp.ac_community_ita.service.ResultImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pending-results")
@RequiredArgsConstructor
public class AdminPendingResultController {

    private final ResultImportService resultImportService;
    private final AdminEventService adminEventService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pendingResults", resultImportService.listPending());
        return "admin/pending-results-list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("preview", resultImportService.preview(id));
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/pending-results";
        }
        model.addAttribute("events", adminEventService.findAll());
        return "admin/pending-result-detail";
    }

    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id,
                          @RequestParam Long eventId,
                          RedirectAttributes redirect) {
        try {
            int created = resultImportService.confirm(id, eventId);
            redirect.addFlashAttribute("message", "Creati " + created + " risultati.");
            return "redirect:/admin/pending-results";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/pending-results/" + id;
        }
    }

    @PostMapping("/{id}/delete")
    public String discard(@PathVariable Long id, RedirectAttributes redirect) {
        resultImportService.discard(id);
        redirect.addFlashAttribute("message", "Risultato scartato.");
        return "redirect:/admin/pending-results";
    }
}
