package com.noosyn.onboarding.dto.auth_dto;

/**
 * Response DTO returned after successful authentication or registration.
 * <p>
 * Contains a single field: an authentication token (typically a JWT) used for
 * authorizing subsequent client requests.
 * </p>
 *
 * @param token the generated authentication token
 */
public record AuthResponse(String token) {}
