package com.legendme.users.svc.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class S2SAuthFilter extends OncePerRequestFilter {
    @Value("${spring.security.s2s-token}")
    private String encodedInternalToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String providedToken = request.getHeader("X-Internal-Token");

        if (providedToken != null) {
            String decodedToken = new String(Base64.getDecoder().decode(encodedInternalToken), StandardCharsets.UTF_8);
            String decodedProvidedToken = new String(Base64.getDecoder().decode(providedToken), StandardCharsets.UTF_8);

            if (decodedProvidedToken.equals(decodedToken)) {
                var auth = new UsernamePasswordAuthenticationToken(
                        "internal-service", null, List.of()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);

    }
}
