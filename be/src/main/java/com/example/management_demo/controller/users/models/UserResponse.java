package com.example.management_demo.controller.users.models;

public record UserResponse(
        Long id,
        String userName,
        String userEmail,
        String userAddress,
        String phoneNumber,
        String status,
        String role,
        TeamInfo team
) {
    public record TeamInfo(
            Long id,
            String teamName
    ) {}
}
