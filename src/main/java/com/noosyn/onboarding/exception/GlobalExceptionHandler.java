package com.noosyn.onboarding.exception;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.noosyn.onboarding.dto.error_dto.ApiErrorResponse;

import lombok.RequiredArgsConstructor;

/**
 * Global exception handler for centralized error processing across the
 * application.
 * <p>
 * This class intercepts exceptions thrown by controllers and provides a
 * consistent
 * error response structure. Using {@link ControllerAdvice} allows it to apply
 * globally to all controller components.
 * </p>
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex) {

        String errorCode = ex.getErrorCode();
        String errorMessage = messageSource.getMessage(errorCode, null, Locale.getDefault());

        ApiErrorResponse body = new ApiErrorResponse(ex.getErrorCode(), errorMessage, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
