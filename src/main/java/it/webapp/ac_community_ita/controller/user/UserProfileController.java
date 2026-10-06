package it.webapp.ac_community_ita.controller.user;

import it.webapp.ac_community_ita.security.UserPrincipal;
import it.webapp.ac_community_ita.dto.steam.UpdateProfileDto;
import it.webapp.ac_community_ita.entity.user.User;
import it.webapp.ac_community_ita.service.steam.SteamAuthenticationService;
import it.webapp.ac_community_ita.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserService userService;
    private final SteamAuthenticationService steamAuthenticationService;

    public UserProfileController(UserService userService, SteamAuthenticationService steamAuthenticationService) {
        this.userService = userService;
        this.steamAuthenticationService = steamAuthenticationService;
    }

    @GetMapping("/update")
    public String showUpdateForm(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        String steamId = userPrincipal.getUser().getSteamId();
        model.addAttribute(
                "updateProfileDto", userService.getUpdateProfileDto(steamId));
        return "update-profile";
    }



    @PostMapping("/update")
    public String updateProfile(@ModelAttribute UpdateProfileDto updateDto,
                                @AuthenticationPrincipal UserPrincipal userPrincipal,
                                HttpServletRequest request, HttpServletResponse response) {
        User updatedUser = userService.updateUserProfile(userPrincipal.getUser().getSteamId(), updateDto);
        steamAuthenticationService.authenticate(updatedUser, request, response); // aggiorna la sessione
        return "redirect:/home";
    }


}