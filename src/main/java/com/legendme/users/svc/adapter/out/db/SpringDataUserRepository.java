package com.legendme.users.svc.adapter.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Esta interfaz extiende JpaRepository para proporcionar operaciones CRUD y de consulta
 * específicas para la entidad UserJpaEntity.
 * Permite buscar usuarios por email y username, así como verificar la existencia
 * de usuarios basándose en estos campos.
 * Los métodos definidos aquí serán implementados automáticamente por Spring Data JPA.
 * @see UserJpaEntity
 * @see JpaRepository
 *
 */
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
