package com.example.management_demo.controller.users;

import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.users.models.UserBulkCreateRequest;
import com.example.management_demo.controller.users.models.UserBulkCreateResponse;
import com.example.management_demo.controller.users.models.UserFilterResponse;
import com.example.management_demo.controller.users.models.UserModelMapper;
import com.example.management_demo.controller.users.models.UserRequest;
import com.example.management_demo.controller.users.models.UserResponse;
import com.example.management_demo.dto.users.User;
import com.example.management_demo.service.users.UserUseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {

    private final UserUseCaseService userUseCaseService;
    private final UserModelMapper userModelMapper;

//    read
    @Override
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userUseCaseService.getAllUsers()
                .stream()
                .map(userModelMapper::toResponse)
                .toList();
    return ResponseEntity.ok(ApiResponse.success("Retrieved all users successfully", users));
    }

    @Override
    public ResponseEntity<ApiResponse<UserFilterResponse>> getUsersWithFilter(
            int page, int size, String sortBy, String direction,
            String userName, String userEmail, String status, String phoneNumber) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<User> userPage = userUseCaseService.getUsersWithFilter(userName, userEmail, phoneNumber, status, pageable);
        
        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(userModelMapper::toResponse)
                .toList();
        
        UserFilterResponse pagedResponse = new UserFilterResponse(
                responses,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Retrieved users with filter successfully", pagedResponse));
    }

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(Long id) {
        var user = userUseCaseService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userModelMapper.toResponse(user)));
    }

//  write

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(UserRequest request) {
        var user = userModelMapper.toDto(request);
        var created = userUseCaseService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("User created successfully", userModelMapper.toResponse(created)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserBulkCreateResponse>> createRandomUsers(UserBulkCreateRequest request) {
        var users = userUseCaseService.createRandomUsers(request.getQuantity());
        List<UserResponse> responses = users.stream()
                .map(userModelMapper::toResponse)
                .toList();
        
        UserBulkCreateResponse result = new UserBulkCreateResponse(
                request.getQuantity(),
                responses.size(),
                request.getQuantity() - responses.size(),
                responses
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(responses.size() + " users created successfully", result));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(Long id, UserRequest request) {
        var user = userModelMapper.toDto(request);
        var updated = userUseCaseService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userModelMapper.toResponse(updated)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(Long id) {
        userUseCaseService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> restrictUser(Long id) {
        var user = userUseCaseService.restrictUser(id);
        return ResponseEntity.ok(ApiResponse.success("User restricted successfully", userModelMapper.toResponse(user)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(Long id) {
        var user = userUseCaseService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", userModelMapper.toResponse(user)));
    }
}
