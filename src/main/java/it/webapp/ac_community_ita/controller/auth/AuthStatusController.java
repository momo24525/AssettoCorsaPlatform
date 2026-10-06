package it.webapp.ac_community_ita.controller.auth;

import it.webapp.ac_community_ita.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthStatusController {

    @GetMapping("/api/auth/status")
    public Map<String, Object> authStatus(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return Map.of("authenticated", false);
        }
        return Map.of(
                "authenticated", true,
                "username", principal.getUsername()
        );
    }
}