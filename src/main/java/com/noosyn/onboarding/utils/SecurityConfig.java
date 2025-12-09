package com.noosyn.onboarding.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Configures Spring Security for the application.
 * <p>
 * This configuration sets up:
 * </p>
 * <ul>
 * <li>Stateless authentication using JWT</li>
 * <li>Custom JWT authentication filter integration</li>
 * <li>Public and secured endpoint rules</li>
 * <li>Password encoding strategy</li>
 * </ul>
 *
 * <p>
 * All authentication is handled via JWT tokens, and server-side sessions
 * are disabled to maintain stateless behavior.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Configures the main Spring Security filter chain.
     * <p>
     * The configuration includes:
     * </p>
     * <ul>
     * <li>Disabling CSRF (since JWT is used instead of cookies)</li>
     * <li>Stateless session management</li>
     * <li>Permitting public access to authentication endpoints</li>
     * <li>Requiring authentication for all other requests</li>
     * <li>Registering the custom JWT filter before the username/password
     * filter</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} instance used to configure security
     *             behavior
     * @return the constructed {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ADMIN can do everything
                        .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        // CUSTOMER and ADMIN both can view products
                        .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Defines the application's password encoding mechanism.
     * <p>
     * Uses {@link BCryptPasswordEncoder}, a strong hashing algorithm suitable for
     * protecting stored passwords.
     * </p>
     *
     * @return a BCrypt-based {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
