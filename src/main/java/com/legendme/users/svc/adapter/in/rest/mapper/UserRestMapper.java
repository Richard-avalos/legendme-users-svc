package com.legendme.users.svc.adapter.in.rest.mapper;

import com.legendme.users.svc.adapter.in.rest.dto.UserRequest;
import com.legendme.users.svc.adapter.in.rest.dto.UserResponse;
import com.legendme.users.svc.domain.model.User;
/**
* Este mapper convierte entre UserRequest y User, y entre User y UserResponse.
* No tiene dependencias externas, por lo que no necesita anotaciones de Spring.
 *
 * @see UserRequest
 * @see UserResponse
 * @see User
 *
 */
public class UserRestMapper {

    /** Convierte un UserRequest a un User.
     * @param request El objeto UserRequest a convertir.
     * @return Un nuevo objeto User con los datos del request.
     */
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

    /** Convierte un User a un UserResponse.
     * @param user El objeto User a convertir.
     * @return Un nuevo objeto UserResponse con los datos del user.
     */
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
