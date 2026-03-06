package com.example.management_demo.controller.users;

import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.users.models.UserBulkCreateRequest;
import com.example.management_demo.controller.users.models.UserBulkCreateResponse;
import com.example.management_demo.controller.users.models.UserFilterResponse;
import com.example.management_demo.controller.users.models.UserRequest;
import com.example.management_demo.controller.users.models.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserAPI {

    @GetMapping
    ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers();

    @GetMapping("/filter")
    ResponseEntity<ApiResponse<UserFilterResponse>> getUsersWithFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String phoneNumber
    );

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request);

    @PostMapping("/generate")
    ResponseEntity<ApiResponse<UserBulkCreateResponse>> createRandomUsers(@Valid @RequestBody UserBulkCreateRequest request);

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id);

    @PatchMapping("/{id}/restrict")
    ResponseEntity<ApiResponse<UserResponse>> restrictUser(@PathVariable Long id);

    @PatchMapping("/{id}/activate")
    ResponseEntity<ApiResponse<UserResponse>> activateUser(@PathVariable Long id);

    @PatchMapping("/{userId}/assign-team")
    ResponseEntity<ApiResponse<UserResponse>> assignTeam(
            @PathVariable Long userId,
            @RequestParam(required = false) Long teamId
    );
}
