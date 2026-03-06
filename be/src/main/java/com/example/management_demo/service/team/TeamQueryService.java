package com.example.management_demo.service.team;

import com.example.management_demo.dto.company.Team;
import com.example.management_demo.repository.database.team.TeamMapper;
import com.example.management_demo.repository.database.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toTeam)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + id));
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toTeam)
                .toList();
    }

    public Page<Team> getTeamsWithFilter(String teamName, Pageable pageable) {
        return teamRepository.filterTeams(teamName, pageable)
                .map(teamMapper::toTeam);
    }
}
