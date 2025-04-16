package com.paygrid.auth.infrastructure.persistence.repository;

import com.paygrid.auth.domain.model.User;
import com.paygrid.auth.domain.repository.UserRepository;
import com.paygrid.auth.infrastructure.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringUserJpa userJpa;

    @Override
    public Optional<User> findById(String id) {
        if (id == null) return Optional.empty();
        return UserEntityMapper.toDomain(userJpa.findById(UUID.fromString(id)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpa.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return UserEntityMapper.toDomain(userJpa.findByEmail(email));
    }

    @Override
    public User save(User user) {
        return UserEntityMapper.toDomain(
                userJpa.save(UserEntityMapper.toEntity(user))
        );
    }
}
