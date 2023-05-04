package com.jpbc.project.repos;

import com.jpbc.project.models.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepo extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findTeamMembersByTeamId(Long id);

    Optional<TeamMember> findByEmail(String email);
    
    boolean existsByEmailOrId(String email, Long id);

    boolean existsByEmail(String email);


}
