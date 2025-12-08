package com.noosyn.onboarding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noosyn.onboarding.dto.auth_dto.AuthResponse;
import com.noosyn.onboarding.dto.auth_dto.LoginRequest;
import com.noosyn.onboarding.dto.auth_dto.RegisterRequest;
import com.noosyn.onboarding.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller that handles user authentication operations, including
 * registration and login.
 * <p>
 * Exposes endpoints under the {@code /auth} path and delegates business logic
 * to {@link AuthService}. Responses contain authentication payloads such as
 * JWT tokens or user information.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    /**
     * Registers a new user account.
     *
     * @param req the registration details including username, password, and any
     *            other required fields
     * @return a {@link ResponseEntity} containing an {@link AuthResponse} with
     *         authentication information for the newly created user
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }

    /**
     * Authenticates a user and returns an authentication response, typically
     * containing a JWT token.
     *
     * @param req the login credentials including username and password
     * @return a {@link ResponseEntity} containing an {@link AuthResponse} with
     *         authentication details if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }
}
