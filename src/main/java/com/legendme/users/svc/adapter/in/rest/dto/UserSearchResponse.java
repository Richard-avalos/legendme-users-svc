package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.List;

/**
 * Está clase representa la respuesta de una búsqueda de usuarios.
 * Se utiliza en endpoints RESTful para devolver una lista de usuarios que coinciden
 * con ciertos criterios de búsqueda, junto con el total de usuarios encontrados.
 *
 * @param users
 * @param total
 */
public record UserSearchResponse(
   List<UserResponse> users,
   int total
) {
}
