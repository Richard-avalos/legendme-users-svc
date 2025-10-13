package com.legendme.users.svc.adapter.out.db;

import com.legendme.users.svc.application.port.out.UserRepository;
import com.legendme.users.svc.domain.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserPersistenceAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user, String password){
        String hashed = password != null ? passwordEncoder.encode(password) : null;
        UserJpaEntity entity = UserPersistenceMapper.toEntity(user, hashed);
        UserJpaEntity saved = springDataUserRepository.save(entity);
        return UserPersistenceMapper.toDomainModel(saved);
    }

    @Override
    public Optional<User> findById(UUID id){
        return springDataUserRepository.findById(id)
                .map(UserPersistenceMapper::toDomainModel);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return springDataUserRepository.findByEmail(email)
                .map(UserPersistenceMapper::toDomainModel);
    }

    @Override
    public Optional<User> findByUsername(String username){
        return springDataUserRepository.findByUsername(username)
                .map(UserPersistenceMapper::toDomainModel);
    }

    @Override
    public List<User> findAll(){
        return springDataUserRepository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomainModel)
                .toList();
    }

    @Override
    public void delete(UUID id){
        springDataUserRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email.toLowerCase());
    }

    @Override
    public boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username.toLowerCase());
    }
}
