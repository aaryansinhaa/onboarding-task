package com.noosyn.onboarding.dto.error_dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private String errorCode;
    private String message;
    LocalDateTime timestamp;
}
