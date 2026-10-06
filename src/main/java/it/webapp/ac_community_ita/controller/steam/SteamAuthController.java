package it.webapp.ac_community_ita.controller.steam;

import it.webapp.ac_community_ita.dto.steam.SteamPlayer;
import it.webapp.ac_community_ita.entity.User;
import it.webapp.ac_community_ita.service.SteamAuthenticationService;
import it.webapp.ac_community_ita.service.steam.SteamOpenIdService;
import it.webapp.ac_community_ita.service.steam.SteamProfileService;
import it.webapp.ac_community_ita.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth/steam")
public class SteamAuthController {

    private final SteamOpenIdService steamOpenIdService;
    private final SteamProfileService steamProfileService;
    private final UserService userService;
    private final SteamAuthenticationService steamAuthenticationService;

    public SteamAuthController(SteamOpenIdService steamOpenIdService,
                               SteamProfileService steamProfileService,
                               UserService userService,
                               SteamAuthenticationService steamAuthenticationService) {
        this.steamOpenIdService = steamOpenIdService;
        this.steamProfileService = steamProfileService;
        this.userService = userService;
        this.steamAuthenticationService = steamAuthenticationService;
    }

    @GetMapping
    public String login() {
        return "redirect:" + steamOpenIdService.buildLoginUrl();
    }

    @GetMapping("/logout") // o @GetMapping se preferisci non usare form
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        steamAuthenticationService.logout(request, response);

        // Il controller si occupa solo della navigation/redirect
        return "redirect:/home";
    }

    @GetMapping("/callback")
    public String callback(HttpServletRequest request, HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {

        Optional<String> steamId = steamOpenIdService.validateAndExtractSteamId(request);

        if (steamId.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Autenticazione Steam fallita");
            return "redirect:/";
        }

        SteamPlayer profile = steamProfileService.fetchProfile(steamId.get());
        User user = userService.findOrCreateFromSteam(profile);

        if (request.getSession(false) != null) {
            request.changeSessionId(); // previene session fixation
        }
        steamAuthenticationService.authenticate(user, request, response);

        return "redirect:/home";
    }


}