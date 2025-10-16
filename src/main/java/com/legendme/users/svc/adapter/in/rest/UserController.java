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
import com.legendme.users.svc.application.service.FindUserService;
import com.legendme.users.svc.application.service.RegisterUserService;
import com.legendme.users.svc.domain.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
/** * Controlador REST para la gestión de usuarios.
 * Este controlador maneja las solicitudes HTTP relacionadas con la creación,
 * actualización, búsqueda y desactivación de usuarios.
 * Utiliza los servicios de la capa de aplicación para realizar las operaciones necesarias
 * y mapea las entidades de dominio a DTOs para las respuestas.
 *
 * Cada  maneja las validaciones necesarias y lanza excepciones en caso de errores.
 * Utiliza UserRestMapper para convertir entre entidades de dominio y DTOs.
 *
 * @see RegisterUserService
 * @see FindUserService
 * @see UserRestMapper
 *
 */
@RestController
@RequestMapping("/legendme/users")
public class UserController {

    /** Servicios inyectados para manejar la lógica de negocio relacionada con usuarios.
     * - registerUserService: Servicio para registrar y gestionar usuarios.
     * - findUserService: Servicio para buscar y consultar usuarios.
     */
    private final RegisterUserService registerUserService;
    private final FindUserService findUserService;

    /** Constructor para inyección de dependencias.
     * @param registerUserService Servicio para registrar y gestionar usuarios.
     * @param findUserService Servicio para buscar y consultar usuarios.
     */
    public UserController(RegisterUserService registerUserService, FindUserService findUserService) {
        this.registerUserService = registerUserService;
        this.findUserService = findUserService;
    }

    /** Crear usuario LOCAL
     * Endpoint para crear un nuevo usuario con autenticación local.
     * Valida los datos de entrada y utiliza el servicio de registro para crear el usuario.
     * @param request DTO con los datos del nuevo usuario.
     * @return DTO con los datos del usuario creado.
     * @throws IllegalArgumentException si los datos de entrada son inválidos.
     * @throws RuntimeException si el email o username ya están en uso.
     */
    @PostMapping("/create")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        User user = registerUserService.registerLocalUser(request);
        return UserRestMapper.toUserResponse(user);
    }

    /** Crear o actualizar usuario GOOGLE
     * Endpoint para crear o actualizar un usuario autenticado con Google.
     * Si el usuario ya existe, actualiza su información; si no, lo crea.
     * @param request DTO con los datos del usuario de Google.
     * @return DTO con los datos del usuario creado o actualizado.
     * @throws IllegalArgumentException si los datos de entrada son inválidos.
     */
    @PostMapping("/create/google-user")
    public UserResponse upsertGoogle(@RequestBody CreateUserRequest request) {
        User user = registerUserService.upsertGoogleUser(request);
        return UserRestMapper.toUserResponse(user);
    }

    /** Buscar todos los usuarios (sin filtros)
     * Endpoint para buscar todos los usuarios en el sistema.
     * Retorna una lista de usuarios y el total encontrado.
     * @return DTO con la lista de usuarios y el total.
     */
    @PostMapping("/search")
    public UserSearchResponse searchUsers() {
        List<User> users = findUserService.findAll();
        return new UserSearchResponse(
                users.stream().map(UserRestMapper::toUserResponse).collect(Collectors.toList()),
                users.size()
        );
    }

    /** Consultar por ID
     * Endpoint para obtener un usuario por su ID.
     * @param id UUID del usuario a consultar.
     * @return DTO con los datos del usuario encontrado.
     * @throws RuntimeException si el usuario no es encontrado.
     */
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return findUserService.findById(id)
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /** Desactivar usuario (soft delete)
     * Endpoint para desactivar un usuario por su ID.
     * Realiza una desactivación lógica sin eliminar el registro de la base de datos.
     * @param id UUID del usuario a desactivar.
     */
    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable UUID id) {
        registerUserService.deactivateUser(id);
    }

    /** Consultar por email
     * Endpoint para obtener un usuario por su email.
     * @param request DTO con el email del usuario a consultar.
     * @return DTO con los datos del usuario encontrado.
     * @throws RuntimeException si el usuario no es encontrado.
     */
    @PostMapping("/by-email")
    public UserResponse getUserByEmail(@RequestBody EmailRequest request) {
        return findUserService.findByEmail(request.email())
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /** Verificar existencia por email
     * Endpoint para verificar si un email ya está registrado en el sistema.
     * @param request DTO con el email a verificar.
     * @return DTO indicando si el email existe o no.
     */
    @PostMapping("/exists-by-email")
    public ExistsResponse existsByEmail(@RequestBody EmailRequest request) {
        boolean exists = findUserService.existsByEmail(request.email());
        return new ExistsResponse(exists);
    }

    /** Consultar por username
     * Endpoint para obtener un usuario por su nombre de usuario (username).
     * @param username Nombre de usuario a consultar.
     * @return DTO con los datos del usuario encontrado.
     * @throws RuntimeException si el usuario no es encontrado.
     */
    @GetMapping("/by-username")
    public UserResponse getUserByUsername(@RequestParam String username) {
        return findUserService.findByUsername(username)
                .map(UserRestMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /** Obtener todos los usuarios
     * Endpoint para obtener una lista de todos los usuarios en el sistema.
     * @return Lista de DTOs con los datos de todos los usuarios.
     */
    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return findUserService.findAll().stream()
                .map(UserRestMapper::toUserResponse)
                .toList();
    }

    /** Actualizar usuario parcialmente
     * Endpoint para actualizar parcialmente los datos de un usuario.
     * Permite modificar solo los campos proporcionados en la solicitud.
     * @param id UUID del usuario a actualizar.
     * @param request DTO con los campos a actualizar.
     * @return DTO con los datos del usuario actualizado.
     * @throws RuntimeException si el usuario no es encontrado o si hay conflictos con email/username.
     */
    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        User user = registerUserService.updateUserPartial(
                id,
                request
        );
        return UserRestMapper.toUserResponse(user);
    }

}
