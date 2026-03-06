package com.example.management_demo.controller.users.models;

import com.example.management_demo.dto.users.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserModelMapper {

    // UserRequest → User DTO
    User toDto(UserRequest userRequest);

    // User DTO → UserResponse
    default UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse.TeamInfo teamInfo = null;
        if (user.team() != null) {
            teamInfo = new UserResponse.TeamInfo(
                    user.team().id(),
                    user.team().teamName()
            );
        }
        
        return new UserResponse(
                user.id(),
                user.userName(),
                user.userEmail(),
                user.userAddress(),
                user.phoneNumber(),
                user.status(),
                user.role(),
                teamInfo
        );
    }
}
