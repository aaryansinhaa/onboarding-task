package com.noosyn.onboarding.dto.auth_dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO used for user login authentication.
 * <p>
 * Contains the username and password provided by the client when attempting
 * to authenticate.
 * </p>
 *
 * @param username the username of the user attempting to log in
 * @param password the user's password
 */
public record LoginRequest(
    @NotBlank(message = "ERR-104")
    String username, 
    @NotBlank(message = "ERR-104")
    String password) {}
