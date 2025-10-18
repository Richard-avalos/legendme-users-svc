package com.legendme.users.svc.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de JPA para la aplicación.
 * Habilita la auditoría JPA, que permite el seguimiento automático
 * de las fechas de creación y actualización de las entidades.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
