package com.legendme.users.svc.adapter.out.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Configuración de seguridad para la aplicación.
 * Configura la autenticación y autorización utilizando JWT (JSON Web Tokens).
 * Define las reglas de acceso a los endpoints y la validación de los tokens JWT.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http            El objeto HttpSecurity para configurar la seguridad HTTP.
     * @param jwtAuthConverter El convertidor de autenticación JWT.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthConverter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("actuator/health").permitAll()
                        .requestMatchers("/legendme/users/create/google-user").permitAll()
                        .requestMatchers("/legendme/users/create").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );
        return http.build();
    }

    /**
     * Configura el decodificador de JWT.
     *
     * @param secret El secreto utilizado para firmar los tokens JWT.
     * @param issuer El emisor esperado de los tokens JWT.
     * @return El decodificador de JWT configurado.
     */
    @Bean
    JwtDecoder jwtDecoder(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer) {
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA384" );
        var decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS384).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);

        var timestampValidator = new JwtTimestampValidator(Duration.ofSeconds(60));

        var validator = new DelegatingOAuth2TokenValidator<>(
                withIssuer, timestampValidator
        );

        decoder.setJwtValidator(validator);
        return decoder;
    }

    /**
     * Configura el convertidor de autenticación JWT.
     *
     * @return El convertidor de autenticación JWT configurado.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthConverter() {
        var conv = new JwtAuthenticationConverter();
        var granted = new JwtGrantedAuthoritiesConverter();

        granted.setAuthoritiesClaimName("roles");
        granted.setAuthorityPrefix("ROLE_");
        conv.setJwtGrantedAuthoritiesConverter(granted);
        return conv;
    }
}