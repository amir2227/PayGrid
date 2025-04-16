package com.paygrid.auth.infrastructure.persistence.mapper;

import com.paygrid.auth.domain.model.Role;
import com.paygrid.auth.domain.model.User;
import com.paygrid.auth.infrastructure.persistence.entity.RoleEntity;
import com.paygrid.auth.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserEntityMapper {

    public static User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId().toString(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles().stream()
                        .map(role -> new Role(role.getId().toString(), role.getName()))
                        .collect(Collectors.toSet())
        );
    }
    public static Optional<User> toDomain(Optional<UserEntity> userEntity) {
        return userEntity.map(UserEntityMapper::toDomain);
    }
    public static UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        UUID id = domain.getId() != null ? UUID.fromString(domain.getId()) : null;
        entity.setId(id);
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());

        Set<RoleEntity> roles = domain.getRoles().stream()
                .map(role -> new RoleEntity(UUID.fromString(role.getId()), role.getName()))
                .collect(Collectors.toSet());

        entity.setRoles(roles);
        return entity;
    }
}
