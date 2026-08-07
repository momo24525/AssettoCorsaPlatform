package it.webapp.ac_community_ita.controller.admin;

import it.webapp.ac_community_ita.dto.CarDto;
import it.webapp.ac_community_ita.entity.CarClass;
import it.webapp.ac_community_ita.service.AdminCarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cars")
@RequiredArgsConstructor
public class AdminCarController {

    private final AdminCarService adminCarService;

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
        model.addAttribute("carDto", new CarDto());
        return "admin/car-form";
    }



    @PostMapping
    public String create(@Valid @ModelAttribute CarDto carDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/car-form";
        }
        try {
            adminCarService.create(carDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "admin/car-form";
        }
        return "redirect:/admin/cars";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        var car = adminCarService.findById(id);
        CarDto dto = new CarDto();
        dto.setName(car.getName());
        dto.setCarClass(car.getCarClass());
        model.addAttribute("carDto", dto);
        model.addAttribute("carId", id);
        return "admin/car-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute CarDto carDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/car-form";
        }
        adminCarService.update(id, carDto);
        return "redirect:/admin/cars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminCarService.delete(id);
        return "redirect:/admin/cars";
    }
}