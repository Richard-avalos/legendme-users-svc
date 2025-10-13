package com.legendme.users.svc.domain.model;

import java.util.Date;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String lastname,
        Date birthDate,
        String username,
        String email,
        String provider,
        boolean active,
        Date createdAt,
        Date updatedAt
) {}
