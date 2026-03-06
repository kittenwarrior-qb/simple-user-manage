package com.example.management_demo.controller.team;

import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.team.models.TeamFilterResponse;
import com.example.management_demo.controller.team.models.TeamModelMapper;
import com.example.management_demo.controller.team.models.TeamRequest;
import com.example.management_demo.controller.team.models.TeamResponse;
import com.example.management_demo.dto.company.Team;
import com.example.management_demo.service.team.TeamUseCaseService;
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
public class TeamController implements TeamAPI {

    private final TeamUseCaseService teamUseCaseService;
    private final TeamModelMapper teamModelMapper;

    @Override
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        List<TeamResponse> teams = teamUseCaseService.getAllTeams()
                .stream()
                .map(teamModelMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Retrieved all teams successfully", teams));
    }

    @Override
    public ResponseEntity<ApiResponse<TeamFilterResponse>> getTeamsWithFilter(
            int page, int size, String sortBy, String direction, String teamName) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Team> teamPage = teamUseCaseService.getTeamsWithFilter(teamName, pageable);
        
        List<TeamResponse> responses = teamPage.getContent()
                .stream()
                .map(teamModelMapper::toResponse)
                .toList();
        
        TeamFilterResponse pagedResponse = new TeamFilterResponse(
                responses,
                teamPage.getNumber(),
                teamPage.getSize(),
                teamPage.getTotalElements(),
                teamPage.getTotalPages(),
                teamPage.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Retrieved teams with filter successfully", pagedResponse));
    }

    @Override
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(Long id) {
        var team = teamUseCaseService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success("Team retrieved successfully", teamModelMapper.toResponse(team)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(TeamRequest request) {
        var team = teamModelMapper.toDto(request);
        var created = teamUseCaseService.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Team created successfully", teamModelMapper.toResponse(created)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(Long id, TeamRequest request) {
        var team = teamModelMapper.toDto(request);
        var updated = teamUseCaseService.updateTeam(id, team);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", teamModelMapper.toResponse(updated)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(Long id) {
        teamUseCaseService.deleteTeam(id);
        return ResponseEntity.ok(ApiResponse.success("Team deleted successfully", null));
    }
}
