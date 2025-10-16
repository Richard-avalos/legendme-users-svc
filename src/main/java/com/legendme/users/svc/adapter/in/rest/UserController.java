package com.legendme.users.svc.adapter.in.rest;

import com.legendme.users.svc.adapter.in.rest.dto.*;
import com.legendme.users.svc.adapter.in.rest.mapper.UserRestMapper;
import com.legendme.users.svc.adapter.out.security.JwtUtils;
import com.legendme.users.svc.application.service.FindUserService;
import com.legendme.users.svc.application.service.RegisterUserService;
import com.legendme.users.svc.domain.model.User;
import com.legendme.users.svc.shared.exceptions.ErrorException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/legendme/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final RegisterUserService registerUserService;
    private final FindUserService findUserService;
    private final JwtUtils jwtUtils;

    public UserController(RegisterUserService registerUserService, FindUserService findUserService, JwtUtils jwtUtils) {
        this.registerUserService = registerUserService;
        this.findUserService = findUserService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/create")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        log.info("Entrada a createUser con request: {}", request.toString());
        try {
            User user = registerUserService.registerLocalUser(request);
            UserResponse response = UserRestMapper.toUserResponse(user);
            log.info("Salida de createUser con respuesta: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en createUser: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/create/google-user")
    public UserResponse upsertGoogle(@RequestBody CreateUserRequest request) {
        log.info("Entrada a upsertGoogle con request: {}", request.toString());
        try {
            User user = registerUserService.upsertGoogleUser(request);
            UserResponse response = UserRestMapper.toUserResponse(user);
            log.info("Salida de upsertGoogle con respuesta: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en upsertGoogle: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/search")
    public UserSearchResponse searchUsers(HttpServletRequest httpRequest) {
        log.info("Entrada a searchUsers");
        try {
            String userId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /search: {}", userId);
            List<User> users = findUserService.findAll();
            UserSearchResponse response = new UserSearchResponse(
                    users.stream().map(UserRestMapper::toUserResponse).collect(Collectors.toList()),
                    users.size()
            );
            log.info("Salida de searchUsers con {} usuarios encontrados", users.size());
            return response;
        } catch (ErrorException e) {
            log.error("Error en searchUsers: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserById con id: {}", id);
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /by-id: {}", authUserId);
            UserResponse response = findUserService.findById(id)
                    .map(UserRestMapper::toUserResponse)
                    .orElseThrow(() -> new ErrorException("Usuario no encontrado"));
            log.info("Salida de getUserById con usuario: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en getUserById: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable UUID id, HttpServletRequest httpRequest) {
        log.info("Entrada a deactivateUser con id: {}", id);
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /deactivate: {}", authUserId);
            registerUserService.deactivateUser(id);
            log.info("Usuario desactivado correctamente: {}", id);
        } catch (ErrorException e) {
            log.error("Error en deactivateUser: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/by-email")
    public UserResponse getUserByEmail(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserByEmail con request: {}", request.toString());
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /by-email: {}", authUserId);
            UserResponse response = findUserService.findByEmail(request.email())
                    .map(UserRestMapper::toUserResponse)
                    .orElseThrow(() -> new ErrorException("Usuario no encontrado"));
            log.info("Salida de getUserByEmail con respuesta: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en getUserByEmail: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/exists-by-email")
    public ExistsResponse existsByEmail(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a existsByEmail con request: {}", request.toString());
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /exists-by-email: {}", authUserId);
            boolean exists = findUserService.existsByEmail(request.email());
            ExistsResponse response = new ExistsResponse(exists);
            log.info("Salida de existsByEmail con resultado: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en existsByEmail: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/by-username/{username}")
    public UserResponse getUserByUsername(@PathVariable String username, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserByUsername con username: {}", username);
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /by-username: {}", authUserId);
            UserResponse response = findUserService.findByUsername(username)
                    .map(UserRestMapper::toUserResponse)
                    .orElseThrow(() -> new ErrorException("Usuario no encontrado"));
            log.info("Salida de getUserByUsername con respuesta: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en getUserByUsername: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers(HttpServletRequest httpRequest) {
        log.info("Entrada a getAllUsers");
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo /all: {}", authUserId);
            List<UserResponse> response = findUserService.findAll()
                    .stream()
                    .map(UserRestMapper::toUserResponse)
                    .toList();
            log.info("Salida de getAllUsers con {} usuarios", response.size());
            return response;
        } catch (ErrorException e) {
            log.error("Error en getAllUsers: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a updateUser con id: {} y request: {}", id, request.toString());
        try {
            String authUserId = jwtUtils.getUserIdFromRequest(httpRequest);
            log.info("Usuario autenticado haciendo PATCH /update: {}", authUserId);
            User user = registerUserService.updateUserPartial(id, request);
            UserResponse response = UserRestMapper.toUserResponse(user);
            log.info("Salida de updateUser con respuesta: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en updateUser: {}", e.getMessage(), e);
            throw e;
        }
    }
}
