package com.paygrid.auth.domain.repository;

import com.paygrid.auth.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User save(User user);
}
