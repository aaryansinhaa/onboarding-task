package com.noosyn.onboarding.utils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Security filter that validates JWT tokens for incoming HTTP requests.
 * <p>
 * This filter executes once per request and performs the following steps:
 * </p>
 * <ul>
 *     <li>Extracts the {@code Authorization} header</li>
 *     <li>Validates the presence and format of the JWT</li>
 *     <li>Extracts the username from the token</li>
 *     <li>Loads the corresponding {@link UserDetails}</li>
 *     <li>Validates the token and sets authentication in the security context</li>
 * </ul>
 *
 * <p>
 * If the token is missing, invalid, or fails validation, the request proceeds
 * without authentication being set.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * Processes incoming requests and applies JWT-based authentication if a valid
     * token is present.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @param chain    the filter chain for continuing request processing
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if request processing fails
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        // Skip if no Bearer token is provided
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username = jwtUtils.extractUsername(token);

        // Authenticate only if no authentication exists in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(token, user)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
