package com.legendme.users.svc.shared.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public ErrorException(String message) {
        this(message, "USR-001", HttpStatus.BAD_REQUEST);
    }

    public ErrorException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String errorCode() {
        return errorCode;
    }

    public HttpStatus status() {
        return status;
    }
}