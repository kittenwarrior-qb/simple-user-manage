package com.example.management_demo.controller.users.models;

import com.example.management_demo.dto.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface UserModelMapper {

    // UserRequest → User DTO
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "role", ignore = true)
    User toDto(UserRequest userRequest);

    // User DTO → UserResponse
    UserResponse toResponse(User user);
}
