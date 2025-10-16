package com.legendme.users.svc.shared.dto;

public record Error(
        int status,
        String message
) {}
