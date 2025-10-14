package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;

public record UpdateUserRequest (
    String name,
    String lastname,
    String username,
    String email,
    Date birthDate
){ }
