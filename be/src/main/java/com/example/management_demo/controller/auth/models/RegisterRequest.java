package com.example.management_demo.controller.auth.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Name is required")
    private String userName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 1 characters")
    private String password;
    
    private String userAddress;
    
    private String phoneNumber;
}
