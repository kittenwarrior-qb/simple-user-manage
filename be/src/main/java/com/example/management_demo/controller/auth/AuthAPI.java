package com.example.management_demo.controller.auth;

import com.example.management_demo.controller.auth.models.AuthResponse;
import com.example.management_demo.controller.auth.models.LoginRequest;
import com.example.management_demo.controller.auth.models.RegisterRequest;
import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.users.models.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthAPI {

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest);

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest);

    @GetMapping("/me")
    ResponseEntity<ApiResponse<UserResponse>> getCurrentUser();

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout();
}
