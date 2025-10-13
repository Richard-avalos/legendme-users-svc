package com.legendme.users.svc.adapter.in.rest.mapper;

import com.legendme.users.svc.adapter.in.rest.dto.UserRequest;
import com.legendme.users.svc.adapter.in.rest.dto.UserResponse;
import com.legendme.users.svc.domain.model.User;

public class UserRestMapper {

    public static User toUser(UserRequest request){
        return new User(
                null,
                request.name(),
                request.lastname(),
                request.birthDate(),
                request.username(),
                request.email(),
                request.provider(),
                true,
                null,
                null
        );
    }

    public static UserResponse toUserResponse(User user){
        return new UserResponse(
                user.id(),
                user.name(),
                user.lastname(),
                user.birthDate(),
                user.username(),
                user.email(),
                user.provider(),
                user.active(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
