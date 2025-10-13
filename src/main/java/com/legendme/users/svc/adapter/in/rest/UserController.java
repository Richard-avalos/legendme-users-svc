package com.legendme.users.svc.adapter.in.rest;

import com.legendme.users.svc.adapter.in.rest.dto.*;
import com.legendme.users.svc.adapter.in.rest.mapper.UserRestMapper;
import com.legendme.users.svc.application.service.FindUserService;
import com.legendme.users.svc.application.service.RegisterUserService;
import com.legendme.users.svc.domain.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/legendme/users")
public class UserController {

    private final RegisterUserService registerUserService;
    private final FindUserService findUserService;

    public UserController(RegisterUserService registerUserService, FindUserService findUserService) {
        this.registerUserService = registerUserService;
        this.findUserService = findUserService;
    }

    // Crear usuario LOCAL
    @PostMapping("/create")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        User user = registerUserService.registerLocalUser(request);
        return UserRestMapper.toUserResponse(user);
    }

    // Crear/actualizar usuario GOOGLE
    @PostMapping("/create/google-user")
    public UserResponse upsertGoogle(@RequestBody CreateUserRequest request) {
        User user = registerUserService.upsertGoogleUser(request);
        return UserRestMapper.toUserResponse(user);
    }

    // Buscar todos (sin filtros)
    @PostMapping("/search")
    public UserSearchResponse searchUsers() {
        List<User> users = findUserService.findAll();
        return new UserSearchResponse(
                users.stream().map(UserRestMapper::toUserResponse).collect(Collectors.toList()),
                users.size()
        );
    }

    // Consultar usuario por ID
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return findUserService.findById(id)
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Desactivar usuario (soft delete)
    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable UUID id) {
        registerUserService.deactivateUser(id);
    }

    // Obtener por email
    @PostMapping("/by-email")
    public UserResponse getUserByEmail(@RequestBody EmailRequest request) {
        return findUserService.findByEmail(request.email())
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Existe email
    @PostMapping("/exists-by-email")
    public ExistsResponse existsByEmail(@RequestBody EmailRequest request) {
        boolean exists = findUserService.existsByEmail(request.email());
        return new ExistsResponse(exists);
    }

    // Consultar usuario por username
    @GetMapping("/by-username")
    public UserResponse getUserByUsername(@RequestParam String username) {
        return findUserService.findByUsername(username)
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Consultar todos
    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return findUserService.findAll().stream()
                .map(UserRestMapper::toUserResponse)
                .toList();
    }

    // Actualizar usuario (parcial)
    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        User user = registerUserService.updateUserPartial(
                id,
                request
        );
        return UserRestMapper.toUserResponse(user);
    }

}
