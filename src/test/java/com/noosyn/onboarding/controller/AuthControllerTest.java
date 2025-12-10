package com.noosyn.onboarding.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noosyn.onboarding.dto.auth_dto.AuthResponse;
import com.noosyn.onboarding.dto.auth_dto.LoginRequest;
import com.noosyn.onboarding.dto.auth_dto.RegisterRequest;
import com.noosyn.onboarding.exception.AppException;
import com.noosyn.onboarding.service.AuthService;
import com.noosyn.onboarding.utils.ApiEndPointConstants;
import com.noosyn.onboarding.utils.JwtAuthenticationFilter;
import com.noosyn.onboarding.utils.JwtUtils;
import com.noosyn.onboarding.utils.SecurityConfig;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtUtils.class,
                JwtAuthenticationFilter.class,
                SecurityConfig.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


// ---------- REGISTER ----------

    @Test
    void ShouldRegisterWhenCorrectInput() throws Exception {
        RegisterRequest req = new RegisterRequest("aaryan", "password123");
        AuthResponse resp = new AuthResponse("fake-jwt-token");

        when(authService.register(req)).thenReturn(resp);

        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authService).register(req);
    }

    @Test
    void ShouldFailRegisterWhenUsernameExists() throws Exception {
        RegisterRequest req = new RegisterRequest("existingUser", "password123");

        when(authService.register(req)).thenThrow(new AppException("ERR-101"));

        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(authService).register(req);
    }

    @Test
    void ShouldFailRegisterWhenInvalidInput() throws Exception {
        RegisterRequest req = new RegisterRequest("", ""); // Invalid input
        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(authService, never()).register(any(RegisterRequest.class));
    }




// ---------- LOGIN ----------
    @Test
    void ShouldLoginWhenCorrectCredentials() throws Exception {
        LoginRequest req = new LoginRequest("aaryan", "password123");
        AuthResponse resp = new AuthResponse("fake-login-jwt");

        when(authService.login(req)).thenReturn(resp);

        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-login-jwt"));

        verify(authService).login(req);
    }
    @Test
    void ShouldFailLoginWhenInvalidCredentials() throws Exception {
        LoginRequest req = new LoginRequest("aaryan", "wrongpassword");

        when(authService.login(req)).thenThrow(new AppException("ERR-102"));

        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isBadRequest());

        verify(authService).login(req);
    }
    @Test
    void ShouldFailLoginWhenInvalidInput() throws Exception {
        LoginRequest req = new LoginRequest("", ""); // Invalid input 
        mockMvc.perform(
                post(ApiEndPointConstants.AUTH_BASE + ApiEndPointConstants.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(authService, never()).login(any(LoginRequest.class));

    }


}


