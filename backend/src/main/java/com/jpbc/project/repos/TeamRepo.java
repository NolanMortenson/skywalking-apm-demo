package com.jpbc.project.repos;

import com.jpbc.project.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamRepo extends JpaRepository<Team, Long> {
    Team findTeamByProjectId(Long id);

    List<Team> findByAdminId(Long id);

    boolean existsTeamByIdOrTeamNameIgnoreCase(Long id, String teamName);

    boolean existsByTeamName(String name);
}
