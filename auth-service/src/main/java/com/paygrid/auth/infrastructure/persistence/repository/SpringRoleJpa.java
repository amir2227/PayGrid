package com.paygrid.auth.infrastructure.persistence.repository;

import com.paygrid.auth.domain.model.vo.RoleType;
import com.paygrid.auth.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringRoleJpa extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(RoleType name);
}
