package com.legendme.users.svc.domain.model;

import java.util.Date;
import java.util.UUID;

/**
 * Representa un usuario en el sistema.
 * Este registro es inmutable y se utiliza para transferir datos de usuario
 * entre diferentes capas de la aplicación.
 *
 * @param id Identificador único del usuario
 * @param name Nombre del usuario
 * @param lastname Apellido del usuario
 * @param birthDate Fecha de nacimiento del usuario
 * @param username Nombre de usuario único
 * @param email Dirección de correo electrónico del usuario
 * @param provider Proveedor de autenticación
 * @param active Estado de la cuenta del usuario
 * @param createdAt Fecha de creación del usuario
 * @param updatedAt Fecha de la última actualización del usuario
 */
public record User(
        UUID id,
        String name,
        String lastname,
        Date birthDate,
        String username,
        String email,
        String provider,
        boolean active,
        Date createdAt,
        Date updatedAt
) {}
