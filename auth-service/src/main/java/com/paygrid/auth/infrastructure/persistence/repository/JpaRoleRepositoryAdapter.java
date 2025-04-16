package com.paygrid.auth.infrastructure.persistence.repository;

import com.paygrid.auth.domain.model.Role;
import com.paygrid.auth.domain.model.vo.RoleType;
import com.paygrid.auth.domain.repository.RoleRepository;
import com.paygrid.auth.infrastructure.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaRoleRepositoryAdapter implements RoleRepository {
    private final SpringRoleJpa roleJpa;
    @Override
    public Optional<Role> findByName(RoleType name) {
        return roleJpa.findByName(name).map(r-> new Role(r.getId().toString(), r.getName()));
    }

    @Override
    public Role save(Role role) {
        UUID id = role.getId() != null ? UUID.fromString(role.getId()) : null;
        RoleEntity roleEntity = roleJpa.save(new RoleEntity(id, role.getName()));
        return new Role(roleEntity.getId().toString(), roleEntity.getName());
    }

}
