package com.legendme.users.svc.shared.dto;
/**
 * Esta clase representa un error que puede ser devuelto por la API.
 * Contiene un código de estado HTTP y un mensaje descriptivo del error.
 *
 * @param status  Código de estado HTTP del error.
 * @param message Mensaje descriptivo del error.
 */
public record Error(
        int status,
        String message
) {}
