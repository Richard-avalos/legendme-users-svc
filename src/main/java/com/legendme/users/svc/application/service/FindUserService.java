package com.legendme.users.svc.application.service;

import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FindUserService {
    private final UserRepository userRepository;

    public FindUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(UUID id){
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email.toLowerCase() );
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username.toLowerCase());
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email.toLowerCase());
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username.toLowerCase());
    }
}
