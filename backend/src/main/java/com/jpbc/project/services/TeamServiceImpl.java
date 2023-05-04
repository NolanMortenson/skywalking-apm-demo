package com.jpbc.project.services;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.exceptionHandler.TeamAlreadyExistsException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.models.Team;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;
    private final TeamMemberRepo teamMemberRepo;
    private final MapstructMapper mapper;

    @Override
    public Team createNewTeam(Team newTeam) throws TeamAlreadyExistsException {
        if (teamRepo.existsTeamByIdOrTeamNameIgnoreCase(newTeam.getId(), newTeam.getTeamName())) {
            throw new TeamAlreadyExistsException();
        }
        return teamRepo.save(newTeam);
    }

    @Override
    public TeamDTO getTeamById(Long id) throws TeamNotFoundException {
        if (!teamRepo.existsById(id)) {
            throw new TeamNotFoundException();
        }

        return mapper.teamToDto(teamRepo.findById(id).orElse(null), teamMemberRepo);
    }

    @Override
    public Team deleteTeam(Long id) throws TeamNotFoundException {
        Team teamToDelete = teamRepo.findById(id).orElse(null);

        if (teamToDelete == null) {
            throw new TeamNotFoundException();
        }

        List<TeamMember> teamUsers = teamMemberRepo.findTeamMembersByTeamId(id);
        teamUsers.forEach(teamUser -> {
            teamUser.setTeam(null);
        });

        teamRepo.deleteById(id);

        return teamToDelete;
    }

    @Override
    public void updateTeam(Team updatedTeam) throws TeamNotFoundException, TeamAlreadyExistsException {


        if (!teamRepo.existsTeamByIdOrTeamNameIgnoreCase(updatedTeam.getId(), updatedTeam.getTeamName())) {
            throw new TeamNotFoundException();
        }

        Team team = teamRepo.findById(updatedTeam.getId()).orElse(null);

        if (teamRepo.existsByTeamName(updatedTeam.getTeamName())) {
            assert team != null;
            if (!Objects.equals(team.getTeamName(), updatedTeam.getTeamName())) {
                throw new TeamAlreadyExistsException();
            }
        }

        team.setTeamName(updatedTeam.getTeamName());
        team.setAdmin(updatedTeam.getAdmin());
        team.setProject(updatedTeam.getProject());

        teamRepo.save(team);
    }

}
