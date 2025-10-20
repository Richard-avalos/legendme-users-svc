package com.legendme.users.svc.shared;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.legendme.users.svc.shared.dto.Error;
import com.legendme.users.svc.shared.exceptions.ErrorException;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura excepciones específicas y devuelve respuestas HTTP adecuadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo ErrorException.
     * Devuelve una respuesta HTTP con el estado y mensaje del error.
     *
     * @param ex  La excepción capturada.
     * @param req La solicitud HTTP que causó la excepción.
     * @return Una respuesta HTTP con el estado y mensaje del error.
     */
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Error> handleErrorException(ErrorException ex, HttpServletRequest req) {
        HttpStatus status = ex.status() != null ? ex.status() : HttpStatus.BAD_REQUEST;
        String errorCode = ex.errorCode() != null ? ex.errorCode() : "error.generic";
        Error body = new Error(
                status.value(),
                ex.getMessage(),
                errorCode
        );
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Maneja las excepciones de tipo IllegalArgumentException.
     * Devuelve una respuesta HTTP con el estado 500 (Internal Status Error) y el mensaje de la excepción.
     *
     * @param ex  La excepción capturada.
     * @param req La solicitud HTTP que causó la excepción.
     * @return Una respuesta HTTP con el estado 400 y el mensaje de la excepción.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Error body = new Error(
                status.value(),
                ex.getMessage(),
                "999"
        );
        return ResponseEntity.status(status).body(body);
    }
}
