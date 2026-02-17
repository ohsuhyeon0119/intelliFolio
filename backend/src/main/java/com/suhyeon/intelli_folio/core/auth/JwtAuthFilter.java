package com.suhyeon.intelli_folio.core.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final Set<String> PUBLIC_PREFIX = Set.of(
            "/api/auth"

    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String p : PUBLIC_PREFIX) {
            if (uri.startsWith(p)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing Bearer token\"}");
            return;
        }

        String token = auth.substring("Bearer ".length()).trim();

        try {
            var verified = authService.verify(token);
            log.info("Auth header = {}", request.getHeader("Authorization"));

            request.setAttribute("userId", verified.userId());
            request.setAttribute("email", verified.email());
        } catch (Exception e) {
            log.warn("JWT rejected: method={} uri={} reason={}",
                    request.getMethod(), request.getRequestURI(), e.toString());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}