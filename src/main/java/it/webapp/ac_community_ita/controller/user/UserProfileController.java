package it.webapp.ac_community_ita.controller.user;

import org.springframework.ui.Model;
import it.webapp.ac_community_ita.config.UserPrincipal;
import it.webapp.ac_community_ita.dto.steam.UpdateProfileDto;
import it.webapp.ac_community_ita.entity.User;
import it.webapp.ac_community_ita.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/update")
    public String showUpdateForm(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        String steamId = userPrincipal.getUser().getSteamId();
        model.addAttribute(
                "updateProfileDto", userService.getUpdateProfileDto(steamId));
        return "update-profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute UpdateProfileDto updateDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String steamId = userPrincipal.getUser().getSteamId();

        User updatedUser = userService.updateUserProfile(steamId, updateDto);

        return "redirect:/home";
    }


}