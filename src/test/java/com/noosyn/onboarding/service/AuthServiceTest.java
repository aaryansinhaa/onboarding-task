package com.noosyn.onboarding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.noosyn.onboarding.dto.auth_dto.AuthResponse;
import com.noosyn.onboarding.dto.auth_dto.LoginRequest;
import com.noosyn.onboarding.dto.auth_dto.RegisterRequest;
import com.noosyn.onboarding.entity.Role;
import com.noosyn.onboarding.entity.User;
import com.noosyn.onboarding.repository.UserRepository;
import com.noosyn.onboarding.utils.JwtUtils;

class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwt;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------- REGISTER TESTS ------------------

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("aaryan", "pass123");
        User user = User.builder()
                .username("aaryan")
                .password("encodedPass")
                .role(Role.USER)
                .build();

        when(repo.findByUsername("aaryan")).thenReturn(Optional.empty());
        when(encoder.encode("pass123")).thenReturn("encodedPass");
        when(repo.save(any(User.class))).thenReturn(user);
        when(jwt.generateToken(any())).thenReturn("jwt-token");

        AuthResponse response = authService.register(req);

        assertEquals("jwt-token", response.token());
        verify(repo).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest req = new RegisterRequest("aaryan", "pass123");

        when(repo.findByUsername("aaryan")).thenReturn(Optional.of(new User()));

        assertThrows(BadCredentialsException.class, () -> authService.register(req));
    }

    // ---------------- LOGIN TESTS ------------------

    @Test
    void testLoginSuccess() {
        LoginRequest req = new LoginRequest("aaryan", "pass123");
        User user = User.builder()
                .username("aaryan")
                .password("encodedPass")
                .role(Role.USER)
                .build();

        when(repo.findByUsername("aaryan")).thenReturn(Optional.of(user));
        when(encoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwt.generateToken(any())).thenReturn("jwt-token");

        AuthResponse response = authService.login(req);

        assertEquals("jwt-token", response.token());
    }

    @Test
    void testLoginInvalidUsername() {
        LoginRequest req = new LoginRequest("wrong", "pass123");

        when(repo.findByUsername("wrong")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequest req = new LoginRequest("aaryan", "wrongPass");
        User user = User.builder()
                .username("aaryan")
                .password("encodedPass")
                .role(Role.USER)
                .build();

        when(repo.findByUsername("aaryan")).thenReturn(Optional.of(user));
        when(encoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }
}
