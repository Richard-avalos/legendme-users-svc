package com.legendme.users.svc.adapter.in.rest.dto;

import java.util.Date;

public record UserRequest(String name, String lastname, Date birthDate, String username, String email, String password, String provider) {

}
