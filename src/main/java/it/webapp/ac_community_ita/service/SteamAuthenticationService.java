package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.config.UserPrincipal;
import it.webapp.ac_community_ita.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class SteamAuthenticationService {

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public void authenticate(User user, HttpServletRequest request, HttpServletResponse response) {
        UserPrincipal principal = new UserPrincipal(user);

        var authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. Recupera la sessione senza crearne una nuova
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Distrugge la sessione lato server
        }
        SecurityContextHolder.clearContext();
    }
}