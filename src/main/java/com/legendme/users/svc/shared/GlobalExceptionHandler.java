package com.legendme.users.svc.shared;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.legendme.users.svc.shared.dto.Error;
import com.legendme.users.svc.shared.exceptions.ErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Error> handleErrorException(ErrorException ex, HttpServletRequest req) {
        HttpStatus status = ex.status() != null ? ex.status() : HttpStatus.BAD_REQUEST;
        Error body = new Error(
                ex.status().value(),
                ex.getMessage()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error body = new Error(
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(status).body(body);
    }
}
