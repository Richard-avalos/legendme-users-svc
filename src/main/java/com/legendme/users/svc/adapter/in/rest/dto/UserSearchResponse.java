package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.List;

public record UserSearchResponse(
   List<UserResponse> users,
   int total
) {
}
