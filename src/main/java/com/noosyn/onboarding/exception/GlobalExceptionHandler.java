package com.noosyn.onboarding.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for centralized error processing across the application.
 * <p>
 * This class intercepts exceptions thrown by controllers and provides a consistent
 * error response structure. Using {@link ControllerAdvice} allows it to apply
 * globally to all controller components.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all uncaught {@link RuntimeException} instances.
     * <p>
     * Returns a standardized error payload containing:
     * </p>
     * <ul>
     *     <li>{@code message} — the exception message</li>
     *     <li>{@code code} — a custom application error code</li>
     *     <li>{@code timestamp} — the moment the error was processed</li>
     * </ul>
     *
     * @param ex the thrown runtime exception
     * @return a {@link ResponseEntity} containing a structured error response
     *         with HTTP status {@link HttpStatus#BAD_REQUEST}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("code", "ERR-001");
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
