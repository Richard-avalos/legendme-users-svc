package com.legendme.users.svc.adapter.out.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 * Entidad JPA que representa a un usuario en la base de datos.
 * Mapea los campos de la tabla "users" y gestiona automáticamente
 * las fechas de creación y actualización mediante auditoría.
 * Utilizada en el contexto de persistencia para operaciones CRUD.
 * Incluye información personal, credenciales y metadatos de la cuenta.
 *
 * @param id identificador único del usuario
 * @param name nombre del usuario
 * @param lastname apellido del usuario
 * @param birthDate fecha de nacimiento del usuario
 * @param username nombre de usuario único
 * @param email correo electrónico del usuario
 * @param password contraseña del usuario
 * @param provider proveedor de autenticación
 * @param active estado de la cuenta
 * @param createdAt fecha de creación de la cuenta
 * @param updatedAt fecha de la última actualización de la cuenta
 *
 */
public class UserJpaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String lastname;
    private Date birthDate;
    private String username;
    private String email;
    private String password;
    private String provider;
    private boolean active;

    @CreatedDate
    @Column(name = "created_at",updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;
}




