package com.legendme.users.svc.adapter.out.db;

import com.legendme.users.svc.adapter.out.db.mapper.UserPersistenceMapper;
import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/** * UserPersistenceAdapter
 * Implementa la interfaz UserRepository para interactuar con la base de datos utilizando Spring Data JPA.
 */
@Component
public class UserPersistenceAdapter implements UserRepository {

    /** Repositorio de datos de usuario basado en Spring Data JPA.
     * Proporciona métodos para realizar operaciones CRUD y consultas específicas en la entidad UserJpaEntity.
     * @see SpringDataUserRepository
     */
    private final SpringDataUserRepository springDataUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserPersistenceAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    /**
     * Guarda un usuario en la base de datos.
     * Si se proporciona una contraseña, se cifra antes de guardarla.
     * @param user El objeto User a guardar.
     * @param password La contraseña en texto plano (opcional).
     * @return El usuario guardado con su ID generado.
     */
    @Override
    public User save(User user, String password){
        String hashed = password != null ? passwordEncoder.encode(password) : null;
        UserJpaEntity entity = UserPersistenceMapper.toEntity(user, hashed);
        UserJpaEntity saved = springDataUserRepository.save(entity);
        return UserPersistenceMapper.toDomainModel(saved);
    }

    /**
     * Busca un usuario por su ID.
     * @param id El UUID del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    @Override
    public Optional<User> findById(UUID id){
        return springDataUserRepository.findById(id)
                .map(UserPersistenceMapper::toDomainModel);
    }

    /**
     * Busca un usuario por su email.
     * @param email El email del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    @Override
    public Optional<User> findByEmail(String email){
        return springDataUserRepository.findByEmail(email)
                .map(UserPersistenceMapper::toDomainModel);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    @Override
    public Optional<User> findByUsername(String username){
        return springDataUserRepository.findByUsername(username)
                .map(UserPersistenceMapper::toDomainModel);
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     * @return Una lista de todos los usuarios.
     */
    @Override
    public List<User> findAll(){
        return springDataUserRepository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomainModel)
                .toList();
    }

    /**
     * Elimina un usuario por su ID.
     * @param id El UUID del usuario a eliminar.
     */
    @Override
    public void delete(UUID id){
        springDataUserRepository.deleteById(id);
    }

    /**
     * Verifica si un usuario existe por su email.
     * @param email El email a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email.toLowerCase());
    }

    /**
     * Verifica si un usuario existe por su nombre de usuario.
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    @Override
    public boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username.toLowerCase());
    }
}
