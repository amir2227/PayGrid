package com.paygrid.auth.domain.repository;

import com.paygrid.auth.domain.model.Role;
import com.paygrid.auth.domain.model.vo.RoleType;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(RoleType name);
    Role save(Role role);
}
