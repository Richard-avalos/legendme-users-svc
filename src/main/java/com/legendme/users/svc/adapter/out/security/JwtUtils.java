package com.legendme.users.svc.adapter.out.security;

import com.legendme.users.svc.shared.exceptions.ErrorException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private final JwtDecoder jwtDecoder;

    public JwtUtils(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public String getUserIdFromRequest(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            throw new ErrorException("Token no encontrado o inválido");
    }
        String token = header.substring(7);
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaim("user_id");
    }

}
