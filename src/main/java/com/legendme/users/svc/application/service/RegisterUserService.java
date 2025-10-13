package com.legendme.users.svc.application.service;

import com.legendme.users.svc.adapter.in.rest.dto.CreateUserRequest;
import com.legendme.users.svc.adapter.in.rest.dto.UpdateUserRequest;
import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterUserService {

    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Crear usuario Local
    public User registerLocalUser(CreateUserRequest request){
        if (!"LOCAL".equalsIgnoreCase(request.provider()))
            throw new IllegalArgumentException("Provider debe ser LOCAL");

        // Validar que el email y username no existan
        if(userRepository.existsByEmail(request.email().toLowerCase())){
            throw new RuntimeException("El email ya está en uso");
        }
        // Validar que el username no exista
        if(userRepository.existsByUsername(request.username().toLowerCase())){
            throw new RuntimeException("El username ya está en uso");
        }

        User user = new User(
                null,
                request.name(),
                request.lastname(),
                request.birthDate(),
                request.username().toLowerCase(),
                request.email().toLowerCase(),
                "LOCAL",
                true,
                new Date(),
                new Date()
        );
        return userRepository.save(user, request.password());
    }

    //Upsert Google
    public  User upsertGoogleUser(CreateUserRequest request){
        if (!"GOOGLE".equalsIgnoreCase(request.provider()))
            throw new IllegalArgumentException("Provider debe ser GOOGLE");

        Optional<User> existingUserOpt = userRepository.findByEmail(request.email().toLowerCase());
        if(existingUserOpt.isPresent()){
            User u = existingUserOpt.get();
            if (!u.provider().equalsIgnoreCase("GOOGLE"))
                throw new RuntimeException("El email ya está en uso con otro proveedor");


        User user = new User(
                null,
                request.name(),
                request.lastname(),
                request.birthDate(),
                request.username().toLowerCase(),
                request.email().toLowerCase(),
                "GOOGLE",
                true,
                new Date(),
                new Date()
        );
        return userRepository.save(user, null);
        } else {
            User user = new User(
                    null,
                    request.name(),
                    request.lastname(),
                    request.birthDate(),
                    request.username().toLowerCase(),
                    request.email().toLowerCase(),
                    "GOOGLE",
                    true,
                    new Date(),
                    new Date()
            );
            return userRepository.save(user, null);
        }
    }
    // Actualizar usuario parcialmente
    public User updateUserPartial(UUID id, UpdateUserRequest request) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        User updated = new User(
                u.id(),
                request.name() != null ? request.name() : u.name(),
                request.lastname() != null ? request.lastname() : u.lastname(),
                request.birthDate() != null ? request.birthDate() : u.birthDate(),
                u.username(),
                u.email(),
                u.provider(),
                u.active(),
                u.createdAt(),
                new Date() // updatedAt
        );

        return userRepository.save(updated,null);
    }
    //Soft delete
    public void deactivateUser(UUID id){
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User deleted = new User(
                u.id(),
                u.name(),
                u.lastname(),
                u.birthDate(),
                u.username(),
                u.email(),
                u.provider(),
                false,
                u.createdAt(),
                new Date()
        );
        userRepository.save(deleted,null);
    }

}


