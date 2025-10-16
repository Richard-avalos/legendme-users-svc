package com.legendme.users.svc.application.port.out;

import com.legendme.users.svc.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la gestión de usuarios.
 * Define las operaciones que se pueden realizar en el repositorio de usuarios.
 * Estas operaciones incluyen guardar, buscar, listar y eliminar usuarios,
 * así como verificar la existencia de usuarios por email o nombre de usuario.
 * Este puerto abstrae la implementación concreta del almacenamiento de datos,
 * permitiendo que la lógica de negocio interactúe con los datos de usuario
 * sin depender de detalles específicos de la base de datos o tecnología utilizada.
 * @see User
 */

public interface UserRepository {
    User save(User user, String password);
    Optional<User>  findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void delete(UUID id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
