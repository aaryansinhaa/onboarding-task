package com.noosyn.onboarding.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final String errorCode;

    public AppException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
