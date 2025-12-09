package com.noosyn.onboarding.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noosyn.onboarding.dto.auth_dto.AuthResponse;
import com.noosyn.onboarding.dto.auth_dto.LoginRequest;
import com.noosyn.onboarding.dto.auth_dto.RegisterRequest;
import com.noosyn.onboarding.entity.Role;
import com.noosyn.onboarding.entity.User;
import com.noosyn.onboarding.exception.AppException;
import com.noosyn.onboarding.repository.UserRepository;
import com.noosyn.onboarding.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

/**
 * Service responsible for handling user authentication logic,
 * including registration, login, and token generation.
 * <p>
 * This service interacts with the {@link UserRepository} for data
 * persistence, {@link PasswordEncoder} for password hashing, and
 * {@link JwtUtils} for issuing JWT authentication tokens.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwt;

    /**
     * Registers a new user in the system.
     * <p>
     * The method performs the following steps:
     * </p>
     * <ul>
     * <li>Ensures the username is not already taken</li>
     * <li>Hashes the provided password</li>
     * <li>Creates a new {@link User} with the role {@code USER}</li>
     * <li>Saves the user to the database</li>
     * <li>Generates a JWT token for the newly registered account</li>
     * </ul>
     *
     * @param req the registration details including username and password
     * @return an {@link AuthResponse} containing the generated JWT token
     * @throws BadCredentialsException if the username is already in use
     */
    public AuthResponse register(RegisterRequest req) {

        if (repo.findByUsername(req.username()).isPresent()) {
            throw new AppException("ERR-101");
        }

        User user = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .role(Role.USER)
                .build();

        repo.save(user);

        return new AuthResponse(jwt.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()));
    }

    /**
     * Authenticates an existing user using provided credentials.
     * <p>
     * The method verifies the username exists and that the provided
     * password matches the stored (encoded) password. If authentication
     * succeeds, a new JWT token is issued.
     * </p>
     *
     * @param req the login request containing username and password
     * @return an {@link AuthResponse} containing a valid JWT token
     * @throws BadCredentialsException if the credentials are invalid
     */
    public AuthResponse login(LoginRequest req) {
        User user = repo.findByUsername(req.username())
                .orElseThrow(() -> new AppException("ERR-102"));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new AppException("ERR-102");
        }

        return new AuthResponse(jwt.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()));
    }
}
