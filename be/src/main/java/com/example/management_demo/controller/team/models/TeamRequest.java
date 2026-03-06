package com.example.management_demo.controller.team.models;

import jakarta.validation.constraints.NotBlank;

public record TeamRequest(
        @NotBlank(message = "Team name is required")
        String teamName,
        
        String description
) {}
