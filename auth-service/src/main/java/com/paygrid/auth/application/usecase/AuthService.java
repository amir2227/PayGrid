package com.paygrid.auth.application.usecase;

import com.paygrid.auth.application.dto.AuthResponse;
import com.paygrid.auth.application.dto.LoginRequest;
import com.paygrid.auth.application.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void register(RegisterRequest request);

    AuthResponse refreshAccessToken(String refreshToken);

}