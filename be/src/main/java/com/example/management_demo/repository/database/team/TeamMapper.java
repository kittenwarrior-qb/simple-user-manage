package com.example.management_demo.repository.database.team;

import com.example.management_demo.dto.company.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team toTeam(TeamEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    TeamEntity toEntity(Team team);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntity(Team team, @MappingTarget TeamEntity entity);
}
