package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.admin.CarFormDto;
import it.webapp.ac_community_ita.entity.car.CarClass;
import it.webapp.ac_community_ita.service.admin.AdminCarService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cars")

public class AdminCarController {

    private final AdminCarService adminCarService;

    public AdminCarController(AdminCarService adminCarService) {
        this.adminCarService = adminCarService;
    }

    @ModelAttribute("carClasses")
    public CarClass[] carClasses() {
        return CarClass.values();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cars", adminCarService.findAll());
        return "admin/cars-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("carDto", new CarFormDto());
        return "admin/car-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute CarFormDto carFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/car-form";
        }
        try {
            adminCarService.create(carFormDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "admin/car-form";
        }
        return "redirect:/admin/cars";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        var car = adminCarService.findById(id);
        CarFormDto dto = new CarFormDto();
        dto.setName(car.getName());
        dto.setCarClass(car.getCarClass());
        model.addAttribute("carDto", dto);
        model.addAttribute("carId", id);
        return "admin/car-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute CarFormDto carFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/car-form";
        }
        adminCarService.update(id, carFormDto);
        return "redirect:/admin/cars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminCarService.delete(id);
        return "redirect:/admin/cars";
    }
}