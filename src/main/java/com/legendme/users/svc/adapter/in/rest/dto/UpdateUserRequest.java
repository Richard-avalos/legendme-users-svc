package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;

public record UpdateUserRequest (
    String name,
    String lastname,
    Date birthDate,
    Boolean active
){ }
