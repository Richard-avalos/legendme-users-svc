package com.legendme.users.svc.application.service;

import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.legendme.users.svc.shared.exceptions.ErrorException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la búsqueda y verificación de usuarios.
 * Proporciona métodos para encontrar usuarios por ID, email o nombre de usuario,
 * así como para listar todos los usuarios y verificar la existencia de usuarios
 * por email o nombre de usuario.
 * Este servicio interactúa con el UserRepository para realizar las operaciones
 * necesarias en el almacenamiento de datos.
 *
 * @see User
 * @see UserRepository
 */
@Slf4j
@Service
public class FindUserService {

    /**
     * Repositorio de usuarios para realizar operaciones de búsqueda y verificación.
     */
    private final UserRepository userRepository;

    /**
     * Constructor para la inyección de dependencias del UserRepository.
     *
     * @param userRepository Repositorio de usuarios.
     */
    public FindUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Buscar un usuario por su ID.
     *
     * @param id UUID del usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findById(UUID id) {
        if (id == null) {
            throw new ErrorException("El ID no puede ser nulo", "USER-FIND-ID-01", HttpStatus.BAD_REQUEST);
        }

        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            log.error("Error al buscar usuarios por ID en BD: {}", e.getMessage());
            throw new ErrorException("Error al buscar usuario por ID", "USER-FIND-ID-02", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar un usuario por su email.
     *
     * @param email Email del usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findByEmail(String email) {
        try {

            if (email == null || email.isBlank()) {
                throw new ErrorException("El email no puede ser nulo o vacío", "USER-FIND-EMAIL-01", HttpStatus.BAD_REQUEST);
            }
            return userRepository.findByEmail(email.toLowerCase());

        } catch (ErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar usuarios por EMAIL: {}", e.getMessage());
            throw new ErrorException("Error al buscar usuario por email", "USER-FIND-EMAIL-02", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findByUsername(String username) {
        try {

            if (username == null || username.isBlank()) {
                throw new ErrorException("El username no puede ser nulo o vacío", "USER-FIND-USERNAME-01", HttpStatus.BAD_REQUEST);
            }
            return userRepository.findByUsername(username.toLowerCase());

        } catch (ErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar usuarios por USERNAME: {}", e.getMessage());
            throw new ErrorException("Error al buscar usuario por username", "USER-FIND-USERNAME-02", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todos los usuarios.
     *
     * @return Lista de todos los usuarios.
     */
    public List<User> findAll() {
        try {
            return userRepository.findAll();
        } catch (ErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener todos los usuarios de BD: {}", e.getMessage());
            throw new ErrorException("Error al obtener todos los usuarios", "USER-FIND-ALL-01", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verificar si un usuario existe por su email.
     *
     * @param email Email a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ErrorException("El email no puede ser nulo o vacío", "USER-EXISTS-EMAIL-01", HttpStatus.BAD_REQUEST);
        }

        try {
            return userRepository.existsByEmail(email.toLowerCase());
        } catch (Exception e) {
            log.error("Error al buscar usuario por EMAIL: {}", e.getMessage());
            throw new ErrorException("Error al verificar existencia de usuario por email", "USER-EXISTS-EMAIL-02", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verificar si un usuario existe por su nombre de usuario.
     *
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existsByUsername(String username) {

        try {

            if (username == null || username.isBlank()) {
                throw new ErrorException("El username no puede ser nulo o vacío", "USER-EXISTS-USERNAME-01", HttpStatus.BAD_REQUEST);
            }
            return userRepository.existsByUsername(username.toLowerCase());

        } catch (ErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar usuario por USERNAME: {}", e.getMessage());
            throw new ErrorException("Error al verificar existencia de usuario por username", "USER-EXISTS-USERNAME-02", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
