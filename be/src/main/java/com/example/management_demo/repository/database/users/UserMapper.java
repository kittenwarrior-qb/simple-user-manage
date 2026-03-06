package com.example.management_demo.repository.database.users;

import com.example.management_demo.dto.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserEntity → User DTO
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "role", expression = "java(entity.getRole().name())")
    User toUser(UserEntity entity);

    // User DTO → UserEntity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(User user);

    // Cập nhật entity từ User DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity entity);
}
