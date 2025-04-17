package com.paygrid.auth.application.usecase;

import com.paygrid.auth.application.dto.AuthResponse;
import com.paygrid.auth.application.dto.LoginRequest;
import com.paygrid.auth.application.dto.RegisterRequest;
import com.paygrid.auth.domain.event.UserRegisteredEvent;
import com.paygrid.auth.domain.model.Role;
import com.paygrid.auth.domain.model.User;
import com.paygrid.auth.domain.model.vo.RoleType;
import com.paygrid.auth.domain.repository.RoleRepository;
import com.paygrid.auth.domain.repository.UserRepository;
import com.paygrid.auth.infrastructure.exception.DuplicatedException;
import com.paygrid.auth.infrastructure.exception.NotFoundException;
import com.paygrid.auth.infrastructure.exception.UnAuthorizedException;
import com.paygrid.auth.infrastructure.security.JwtTokenProvider;
import com.paygrid.auth.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        UserDetails userDetails = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String accessToken = jwtTokenProvider.generateAccessToken(auth);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicatedException("Email already exists");
        }
        Role defaultRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new NotFoundException("Default role not found"));
        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Set.of(defaultRole)
        );
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(UUID.fromString(savedUser.getId()), savedUser.getEmail()));

    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnAuthorizedException("Invalid refresh token"));
        UserDetails userDetails = new UserPrincipal(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
                );
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
