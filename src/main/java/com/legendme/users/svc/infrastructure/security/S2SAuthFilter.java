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

/**
 * S2SAuthFilter is a custom Spring Security filter that validates requests
 * based on an internal token provided in the `X-Internal-Token` header.
 *
 * This filter decodes and compares the provided token with a pre-configured
 * internal token. If the tokens match, it sets an authentication object in the
 * SecurityContext, allowing the request to proceed as an authenticated internal service.
 */
@Component
public class S2SAuthFilter extends OncePerRequestFilter {
    /**
     * The base64-encoded internal token used for authentication.
     * This value is injected from the application properties.
     */
    @Value("${spring.security.s2s-token}")
    private String encodedInternalToken;

    /**
     * Filters incoming requests to validate the `X-Internal-Token` header.
     *
     * If the provided token matches the decoded internal token, an
     * authentication object is created and set in the SecurityContext.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to pass the request and response to the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
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
