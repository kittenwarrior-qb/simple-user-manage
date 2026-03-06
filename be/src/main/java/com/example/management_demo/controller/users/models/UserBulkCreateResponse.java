package com.example.management_demo.controller.users.models;

import java.util.List;

public record UserBulkCreateResponse(
        int totalRequested,
        int successCount,
        int failCount,
        List<UserResponse> createdUsers
) {
}
