package com.example.management_demo.dto.users;

import com.example.management_demo.dto.company.Team;

public record User(
        Long id,
        String userName,
        String userEmail,
        String password,
        String userAddress,
        String phoneNumber,
        String status,
        String role,
        Team team
) {
}