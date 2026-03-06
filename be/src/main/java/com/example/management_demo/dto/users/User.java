package com.example.management_demo.dto.users;

public record User(
        Long id,
        String userName,
        String userEmail,
        String password,
        String userAddress,
        String phoneNumber,
        String status,
        String role
) {
}