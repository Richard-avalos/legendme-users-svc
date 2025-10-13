package com.legendme.users.svc.adapter.out.db;

import com.legendme.users.svc.domain.model.User;

public class UserPersistenceMapper {

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
