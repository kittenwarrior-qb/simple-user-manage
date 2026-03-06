package com.example.management_demo.controller.auth;

import com.example.management_demo.controller.auth.models.AuthResponse;
import com.example.management_demo.controller.auth.models.LoginRequest;
import com.example.management_demo.controller.auth.models.RegisterRequest;
import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.users.models.UserResponse;
import com.example.management_demo.dto.users.User;
import com.example.management_demo.service.auth.AuthUseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthAPI {

    private final AuthUseCaseService authUseCaseService;

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        try {
            String jwt = authUseCaseService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new AuthResponse(jwt, "Login successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        try {
            String jwt = authUseCaseService.register(
                registerRequest.getUserName(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getUserAddress(),
                registerRequest.getPhoneNumber()
            );
            return ResponseEntity.status(201).body(new AuthResponse(jwt, "Registration successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new AuthResponse(null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        User user = authUseCaseService.getCurrentUser();
        
        UserResponse userResponse = new UserResponse(
            user.id(),
            user.userName(),
            user.userEmail(),
            user.userAddress(),
            user.phoneNumber(),
            user.status(),
            user.role()
        );
        
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userResponse));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> logout() {
        authUseCaseService.logout();
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
}
