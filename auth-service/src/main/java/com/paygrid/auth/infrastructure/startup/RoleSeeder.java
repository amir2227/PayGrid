package com.paygrid.auth.infrastructure.startup;

import com.paygrid.auth.domain.model.Role;
import com.paygrid.auth.domain.model.vo.RoleType;
import com.paygrid.auth.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(ApplicationArguments args) {
        Arrays.stream(RoleType.values()).forEach(roleType -> roleRepository.findByName(roleType)
                .orElseGet(() -> roleRepository.save(new Role(null, roleType))));
    }
}
