package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;
/**
 * DTO para actualizar los datos personales de un usuario registrado.
 *
 * Este record se utiliza en endpoints RESTful para recibir solicitudes de modificación
 * de información básica del usuario. No incluye cambios de contraseña ni estado de la cuenta.
 *
 * Es útil en operaciones como edición de perfil o actualización de datos personales.
 *
 * @param name nuevo nombre del usuario
 * @param lastname nuevo apellido del usuario
 * @param username nuevo nombre de usuario
 * @param email nuevo correo electrónico
 * @param birthDate nueva fecha de nacimiento
 */


public record UpdateUserRequest (
    String name,
    String lastname,
    String username,
    String email,
    Date birthDate
){ }
