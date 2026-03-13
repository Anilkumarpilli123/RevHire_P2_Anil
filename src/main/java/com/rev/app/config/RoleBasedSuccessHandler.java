package com.rev.app.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class RoleBasedSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrl = "/";

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_SEEKER".equals(role)) {
                targetUrl = "/seeker/dashboard";
                break;
            } else if ("ROLE_EMPLOYER".equals(role)) {
                targetUrl = "/employer/dashboard";
                break;
            }
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
