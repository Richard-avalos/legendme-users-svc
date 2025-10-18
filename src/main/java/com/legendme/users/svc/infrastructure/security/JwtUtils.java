package com.legendme.users.svc.infrastructure.security;

import com.legendme.users.svc.shared.exceptions.ErrorException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

/**
 * Esta clase proporciona utilidades para manejar JSON Web Tokens (JWT) en una aplicación Spring.
 * Permite decodificar tokens JWT y extraer información específica, como el ID de usuario,
 * desde las solicitudes HTTP entrantes.
 * Utiliza un JwtDecoder para decodificar el token y maneja errores si el
 */
@Component
public class JwtUtils {

    /**
     * Decodificador de JWT para procesar los tokens.
     */
    private final JwtDecoder jwtDecoder;

    /**
     * Constructor para la inyección de dependencias del JwtDecoder.
     *
     * @param jwtDecoder Decodificador de JWT.
     */
    public JwtUtils(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Extrae el ID de usuario del token JWT presente en la cabecera Authorization de la solicitud HTTP.
     *
     * @param request La solicitud HTTP que contiene el token JWT.
     * @return El ID de usuario extraído del token.
     * @throws ErrorException si el token no está presente, es inválido o no contiene el ID de usuario.
     */
    public String getUserIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ErrorException("Token no encontrado o inválido");
        }
        String token = header.substring(7);
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaim("user_id");
    }

}
