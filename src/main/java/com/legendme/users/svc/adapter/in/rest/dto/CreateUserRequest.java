package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;

public record CreateUserRequest (
    String name,
    String lastname,
    String username,
    Date birthDate,
    String email,
    String provider,
    Boolean active,
    String password
) {}
