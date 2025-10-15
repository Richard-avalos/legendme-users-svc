package com.legendme.users.svc.adapter.in.rest.dto;

/**
 * DTO que representa una solicitud que contiene un correo electrónico.
 *
 * Este objeto se utiliza para operaciones que requieren validar, buscar o verificar
 * la existencia de un usuario a partir de su dirección de correo electrónico.
 *
 * @param email La dirección de correo electrónico del usuario
 */
public record EmailRequest(
    String email
) {
}
