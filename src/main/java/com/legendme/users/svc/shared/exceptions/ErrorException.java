package com.legendme.users.svc.shared.exceptions;

import org.springframework.http.HttpStatus;
/**
 * Excepción personalizada para manejar errores en la aplicación.
 * Contiene un código de error y un estado HTTP asociado.
 */
public class ErrorException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    /**
     * Constructor que crea una excepción con un mensaje, un código de error y un estado HTTP específicos.
     *
     * @param message   Mensaje descriptivo del error.
     * @param errorCode Código de error específico.
     * @param status    Estado HTTP asociado al error.
     */
    public ErrorException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    /**
     * Obtiene el código de error asociado a la excepción.
     *
     * @return Código de error.
     */
    public String errorCode() {
        return errorCode;
    }

    /**
     * Obtiene el estado HTTP asociado a la excepción.
     *
     * @return Estado HTTP.
     */
    public HttpStatus status() {
        return status;
    }
}