package com.example.management_demo.controller.auth.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Format Email is incorrect")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
