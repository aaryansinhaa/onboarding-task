package com.noosyn.onboarding.dto.auth_dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO used for registering a new user account.
 * <p>
 * Contains the necessary information to create a new user, such as
 * username and password. Additional fields may be added as needed.
 * </p>
 *
 * @param username the username for the new account
 * @param password the password for the new account
 */
public record RegisterRequest(
    @NotBlank(message = "ERR-104")   // or your custom error code
    String username,

    @NotBlank(message = "ERR-104")
    String password
) {}
