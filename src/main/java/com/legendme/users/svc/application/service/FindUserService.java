package com.legendme.users.svc.application.service;

import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
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
 * @see User
 * @see UserRepository
 */
@Service
public class FindUserService {

    /** Repositorio de usuarios para realizar operaciones de búsqueda y verificación. */
    private final UserRepository userRepository;

    /** Constructor para la inyección de dependencias del UserRepository.
     * @param userRepository Repositorio de usuarios.
     */
    public FindUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Buscar un usuario por su ID.
     * @param id UUID del usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findById(UUID id){
        if (id == null) {
            throw new ErrorException("El ID no puede ser nulo");
        }
        return userRepository.findById(id);
    }

    /** Buscar un usuario por su email.
     * @param email Email del usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findByEmail(String email){
        if(email == null || email.isBlank()){
            throw new ErrorException("El email no puede ser nulo o vacío");
        }
        return userRepository.findByEmail(email.toLowerCase() );
    }

    /** Buscar un usuario por su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    public Optional<User> findByUsername(String username){
        if(username == null || username.isBlank()){
            throw new ErrorException("El username no puede ser nulo o vacío");
        }
        return userRepository.findByUsername(username.toLowerCase());
    }

    /** Listar todos los usuarios.
     * @return Lista de todos los usuarios.
     */
    public List<User> findAll(){
        return userRepository.findAll();
    }

    /** Verificar si un usuario existe por su email.
     * @param email Email a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existsByEmail(String email){
        if(email == null || email.isBlank()){
            throw new ErrorException("El email no puede ser nulo o vacío");
        }
        return userRepository.existsByEmail(email.toLowerCase());
    }

    /** Verificar si un usuario existe por su nombre de usuario.
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existsByUsername(String username){
        if(username == null || username.isBlank()){
            throw new ErrorException("El username no puede ser nulo o vacío");
        }
        return userRepository.existsByUsername(username.toLowerCase());
    }
}
