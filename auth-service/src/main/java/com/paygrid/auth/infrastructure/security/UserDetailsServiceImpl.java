package com.paygrid.auth.infrastructure.security;

import com.paygrid.auth.domain.model.User;
import com.paygrid.auth.infrastructure.persistence.mapper.UserEntityMapper;
import com.paygrid.auth.infrastructure.persistence.repository.SpringUserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SpringUserJpa userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = UserEntityMapper.toDomain( userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+username)));
        return new UserPrincipal(user);
    }
}
