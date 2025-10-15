package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;

/**
 * DTO para solicitudes de creación de usuarios.
 *
 * Este record encapsula los datos necesarios para registrar un nuevo usuario en el sistema,
 * incluyendo información personal, autenticación y estado de la cuenta.
 * Se utiliza en endpoints RESTful para recibir datos de registro.
 *
 * @param name nombre del usuario
 * @param lastname apellido del usuario
 * @param username nombre de usuario único
 * @param birthDate fecha de nacimiento
 * @param email correo electrónico
 * @param provider proveedor de autenticación
 * @param active estado de la cuenta
 * @param password contraseña del usuario
 */


public record CreateUserRequest (
    String name,
    String lastname,
    String username,
    Date birthDate,
    String email,
    String provider,
    Boolean active,
    String password
) {}
