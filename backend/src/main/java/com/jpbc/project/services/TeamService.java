package com.jpbc.project.services;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.exceptionHandler.TeamAlreadyExistsException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.models.Team;

public interface TeamService {
    Team createNewTeam(Team newTeam) throws TeamAlreadyExistsException;

    TeamDTO getTeamById(Long id) throws TeamNotFoundException;

    Team deleteTeam(Long id) throws TeamNotFoundException;

    void updateTeam(Team newTeam) throws TeamNotFoundException, TeamAlreadyExistsException;
}
