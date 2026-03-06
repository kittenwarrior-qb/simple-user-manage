package com.example.management_demo.controller.team;

import com.example.management_demo.controller.common.ApiResponse;
import com.example.management_demo.controller.team.models.TeamFilterResponse;
import com.example.management_demo.controller.team.models.TeamRequest;
import com.example.management_demo.controller.team.models.TeamResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/teams")
public interface TeamAPI {

    @GetMapping
    ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams();

    @GetMapping("/filter")
    ResponseEntity<ApiResponse<TeamFilterResponse>> getTeamsWithFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String teamName
    );

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ApiResponse<TeamResponse>> createTeam(@Valid @RequestBody TeamRequest request);

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long id);
}
