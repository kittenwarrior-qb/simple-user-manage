package com.example.management_demo.service.team;

import com.example.management_demo.dto.company.Team;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeamUseCaseService {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

    @Transactional
    public Team createTeam(Team team) {
        return teamCommandService.createTeam(team);
    }

    @Transactional
    public Team updateTeam(Long id, Team team) {
        return teamCommandService.updateTeam(id, team);
    }

    @Transactional
    public void deleteTeam(Long id) {
        teamCommandService.deleteTeam(id);
    }

    public Team getTeamById(Long id) {
        return teamQueryService.getTeamById(id);
    }

    public List<Team> getAllTeams() {
        return teamQueryService.getAllTeams();
    }

    public Page<Team> getTeamsWithFilter(String teamName, Pageable pageable) {
        return teamQueryService.getTeamsWithFilter(teamName, pageable);
    }
}
