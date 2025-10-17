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

/**
 * Esta clase es un controlador REST que maneja las solicitudes relacionadas con los usuarios.
 * Proporciona endpoints para crear, buscar, actualizar y desactivar usuarios.
 * Utiliza servicios de aplicación para realizar las operaciones necesarias y mapea
 * los datos entre los modelos de dominio y los DTOs de la API REST.
 */
@RestController
@RequestMapping("/legendme/users")
public class UserController {

    /**
     * Logger para registrar eventos y errores en el controlador.
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    /**
     * Servicio para registrar y gestionar usuarios.
     */
    private final RegisterUserService registerUserService;
    /**
     * Servicio para buscar y verificar usuarios.
     */
    private final FindUserService findUserService;
    /**
     * Utilidad para manejar JWT y extraer información del token.
     */
    private final JwtUtils jwtUtils;

    /**
     * Constructor para la inyección de dependencias de los servicios y utilidades necesarias.
     *
     * @param registerUserService Servicio para registrar y gestionar usuarios.
     * @param findUserService     Servicio para buscar y verificar usuarios.
     * @param jwtUtils            Utilidad para manejar JWT.
     */
    public UserController(RegisterUserService registerUserService, FindUserService findUserService, JwtUtils jwtUtils) {
        this.registerUserService = registerUserService;
        this.findUserService = findUserService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Endpoint para crear un nuevo usuario con autenticación local.
     *
     * @param request DTO con los datos del nuevo usuario.
     * @return DTO con los datos del usuario creado.
     */
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

    /**
     * Endpoint para insertar o actualizar un usuario autenticado con Google.
     *
     * @param request DTO con los datos del usuario de Google.
     * @return DTO con los datos del usuario creado o actualizado.
     */
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

    /**
     * Endpoint para buscar todos los usuarios.
     *
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO con la lista de usuarios encontrados y el total.
     */
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

    /**
     * Endpoint para buscar un usuario por su ID.
     *
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO con los datos del usuario encontrado.
     */
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserById con id: {}", id);
        try {
            log.info("Pedido de busqueda por ID");
            UserResponse response = findUserService.findById(id)
                    .map(UserRestMapper::toUserResponse)
                    .orElseThrow(() -> new ErrorException("Usuario no encontrado"));
            log.info("Salida de getUserById con usuario: {}", response.toString());
            return response;
        } catch (Exception e) {
            log.error("Error en getUserById: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para desactivar un usuario por su ID.
     *
     * @param id          UUID del usuario a desactivar.
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     */
    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable UUID id, HttpServletRequest httpRequest) {
        log.info("Entrada a deactivateUser con id: {}", id);
        try {
            registerUserService.deactivateUser(id);
            log.info("Usuario desactivado correctamente: {}", id);
        } catch (ErrorException e) {
            log.error("Error en deactivateUser: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para buscar un usuario por su email.
     *
     * @param request     DTO con el email del usuario a buscar.
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO con los datos del usuario encontrado.
     */
    @PostMapping("/by-email")
    public UserResponse getUserByEmail(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserByEmail con request: {}", request.toString());
        try {
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

    /**
     * Endpoint para verificar si un usuario existe por su email.
     *
     * @param request     DTO con el email a verificar.
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO indicando si el usuario existe o no.
     */
    @PostMapping("/exists-by-email")
    public ExistsResponse existsByEmail(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a existsByEmail con request: {}", request.toString());
        try {
            boolean exists = findUserService.existsByEmail(request.email());
            ExistsResponse response = new ExistsResponse(exists);
            log.info("Salida de existsByEmail con resultado: {}", response.toString());
            return response;
        } catch (ErrorException e) {
            log.error("Error en existsByEmail: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para buscar un usuario por su nombre de usuario.
     *
     * @param username    Nombre de usuario a buscar.
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO con los datos del usuario encontrado.
     */
    @GetMapping("/by-username/{username}")
    public UserResponse getUserByUsername(@PathVariable String username, HttpServletRequest httpRequest) {
        log.info("Entrada a getUserByUsername con username: {}", username);
        try {
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

    /**
     * Endpoint para obtener todos los usuarios.
     *
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return Lista de DTOs con los datos de todos los usuarios.
     */
    @GetMapping("/all")
    public List<UserResponse> getAllUsers(HttpServletRequest httpRequest) {
        log.info("Entrada a getAllUsers");
        try {
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

    /**
     * Endpoint para actualizar parcialmente un usuario por su ID.
     *
     * @param request     DTO con los campos a actualizar (pueden ser nulos).
     * @param httpRequest Solicitud HTTP para extraer el token JWT.
     * @return DTO con los datos del usuario actualizado.
     */
    @PatchMapping("/update")
    public UserResponse updateUser(@RequestBody UpdateUserRequest request, HttpServletRequest httpRequest) {
        log.info("Entrada a updateUser con request: {}", request);

        try {
            UUID authUserId = UUID.fromString(jwtUtils.getUserIdFromRequest(httpRequest));
            log.info("Usuario autenticado haciendo PATCH /update: {}", authUserId);

            User updatedUser = registerUserService.updateUserPartial(authUserId, request);

            UserResponse response = UserRestMapper.toUserResponse(updatedUser);
            log.info("Salida de updateUser con respuesta: {}", response);
            return response;

        } catch (ErrorException e) {
            log.error("Error en updateUser: {}", e.getMessage(), e);
            throw e;
        }
    }

}
