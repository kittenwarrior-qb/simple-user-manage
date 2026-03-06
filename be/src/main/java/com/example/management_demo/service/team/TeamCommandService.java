package com.example.management_demo.service.team;

import com.example.management_demo.dto.company.Team;
import com.example.management_demo.repository.database.team.TeamEntity;
import com.example.management_demo.repository.database.team.TeamMapper;
import com.example.management_demo.repository.database.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public Team createTeam(Team team) {
        log.debug("Creating team with name: {}", team.teamName());
        
        if (teamRepository.existsByTeamName(team.teamName())) {
            log.warn("Failed to create team: Team name already in use - {}", team.teamName());
            throw new IllegalArgumentException("Team name already in use: " + team.teamName());
        }
        
        TeamEntity entity = teamMapper.toEntity(team);
        Team created = teamMapper.toTeam(teamRepository.save(entity));
        log.info("Team created successfully with name: {}", created.teamName());
        return created;
    }

    public Team updateTeam(Long id, Team team) {
        log.debug("Updating team with id: {}", id);
        
        TeamEntity entity = teamRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to update: Team not found with id: {}", id);
                    return new IllegalArgumentException("Team not found with id: " + id);
                });
        
        teamMapper.updateEntity(team, entity);
        Team updated = teamMapper.toTeam(teamRepository.save(entity));
        log.info("Team updated successfully: {}", id);
        return updated;
    }

    public void deleteTeam(Long id) {
        log.debug("Deleting team with id: {}", id);
        
        if (!teamRepository.existsById(id)) {
            log.warn("Failed to delete: Team not found with id: {}", id);
            throw new IllegalArgumentException("Team not found with id: " + id);
        }
        
        teamRepository.deleteById(id);
        log.info("Team deleted successfully: {}", id);
    }
}
