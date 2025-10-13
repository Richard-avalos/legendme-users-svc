package com.legendme.users.svc.application.port.out;

import com.legendme.users.svc.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user, String password);
    Optional<User>  findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void delete(UUID id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
