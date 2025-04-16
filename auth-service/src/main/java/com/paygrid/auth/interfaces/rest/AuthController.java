package com.paygrid.auth.interfaces.rest;

import com.paygrid.auth.application.dto.AuthResponse;
import com.paygrid.auth.application.dto.LoginRequest;
import com.paygrid.auth.application.dto.RefreshTokenRequest;
import com.paygrid.auth.application.dto.RegisterRequest;
import com.paygrid.auth.application.usecase.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.paygrid.auth.interfaces.rest.AuthController.BASE_URL;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    public final static String BASE_URL = "/api/v1/auth";
    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest){
         authService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @Operation(summary = "Login user", description = "Authenticates the user and returns a JWT token.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Refresh token", description = "Refreshes the JWT token.")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
