package com.noosyn.onboarding.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
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

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex, Locale locale) {

        Map<String, Object> body = new HashMap<>();
        String key = ex.getMessage();
        String message = messageSource.getMessage(
                key,
                null,
                key, 
                locale
        );
        String code = messageSource.getMessage(
                "error.code." + key.substring(key.indexOf('.') + 1),
                null,
                "unknown error code",
                locale
        );
        body.put("message", message);
        body.put("code", code);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
