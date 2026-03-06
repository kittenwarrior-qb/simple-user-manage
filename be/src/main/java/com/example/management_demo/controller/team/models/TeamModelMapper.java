package com.example.management_demo.controller.team.models;

import com.example.management_demo.dto.company.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamModelMapper {

    Team toDto(TeamRequest teamRequest);

    TeamResponse toResponse(Team team);
}
