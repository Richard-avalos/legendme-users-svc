package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;
import java.util.UUID;

public record UserResponse(UUID id, String name, String lastname, Date birthDate, String username, String email, String provider, boolean active, Date createdAt, Date updatedAt) {
}
