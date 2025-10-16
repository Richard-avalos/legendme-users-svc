package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;
import java.util.UUID;
/**
*Esta clase representa la respuesta que se envía al cliente cuando se solicita información de un usuario.
* Se utiliza en endpoints RESTful para devolver los detalles de un usuario específico.
* Incluye información personal, credenciales y metadatos de la cuenta.
*
* @param id identificador único del usuario
* @param name nombre del usuario
* @param lastname apellido del usuario
* @param birthDate fecha de nacimiento del usuario
* @param username nombre de usuario único
* @param email correo electrónico del usuario
* @param provider proveedor de autenticación
* @param active estado de la cuenta
* @param createdAt fecha de creación de la cuenta
* @param updatedAt fecha de la última actualización de la cuenta
 */
public record UserResponse(UUID id, String name, String lastname, Date birthDate, String username, String email, String provider, boolean active, Date createdAt, Date updatedAt) {
}
