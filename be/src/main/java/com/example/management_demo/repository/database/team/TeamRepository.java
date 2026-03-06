package com.example.management_demo.repository.database.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    boolean existsByTeamName(String teamName);

    @Query("SELECT t FROM TeamEntity t WHERE " +
            "(:teamName IS NULL OR LOWER(t.teamName) LIKE LOWER(CONCAT('%', :teamName, '%')))")
    Page<TeamEntity> filterTeams(@Param("teamName") String teamName, Pageable pageable);
}
