package com.example.management_demo.controller.users.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank(message = "Username is required")
        String userName,
        
        @NotBlank(message = "Email is required")
        String userEmail,
        
        @NotBlank(message = "Password is required")
        String password,
        
        String userAddress,
        
        @Pattern(regexp = "^0\\d{1,10}$", message = "Phone number must be starting with 0")
        String phoneNumber
) {}
