package com.legendme.users.svc.adapter.out.db.mapper;

import com.legendme.users.svc.adapter.out.db.UserJpaEntity;
import com.legendme.users.svc.domain.model.User;

/**
 * Este mapper se encarga de convertir entre la entidad JPA y el modelo de dominio.
 * Proporciona métodos estáticos para facilitar la conversión sin necesidad de instanciar la clase.
 * No tiene dependencias externas, por lo que no requiere anotaciones de Spring.
 *
 * @see UserJpaEntity
 * @see User
 */
public class UserPersistenceMapper {

    /** Convierte una entidad JPA a un modelo de dominio.
     * @param entity La entidad UserJpaEntity a convertir.
     * @return Un nuevo objeto User con los datos de la entidad.
     */
    public static User toDomainModel(UserJpaEntity entity){
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getLastname(),
                entity.getBirthDate(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getProvider(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /** Convierte un modelo de dominio a una entidad JPA.
     * @param user El objeto User a convertir.
     * @param hashedPassword La contraseña hasheada del usuario (puede ser null si no se actualiza).
     * @return Un nuevo objeto UserJpaEntity con los datos del user.
     */
    public static UserJpaEntity toEntity(User user, String hashedPassword){
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setLastname(user.lastname());
        entity.setBirthDate(user.birthDate());
        entity.setUsername(user.username());
        entity.setEmail(user.email());
        if(hashedPassword != null) entity.setPassword(hashedPassword);
        entity.setProvider(user.provider());
        entity.setActive(user.active());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }
}
