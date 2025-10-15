package com.legendme.users.svc.adapter.in.rest.dto;
/**
 * DTO que representa la respuesta sobre la existencia de un recurso.
 *
 * Se utiliza para indicar si un elemento (como un usuario, correo electrónico o nombre de usuario)
 * existe en el sistema.
 *
 * @param exists valor booleano que indica si el recurso existe (true) o no (false)
 */
public record ExistsResponse(boolean exists) {

}
