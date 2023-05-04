package com.jpbc.project.services;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.exceptionHandler.UserAlreadyExistsException;
import com.jpbc.project.exceptionHandler.UserNotFoundException;
import com.jpbc.project.models.TeamMember;

import java.util.List;


public interface TeamMemberService {


    public List<TeamMemberDTO> getAllAdmins();

    TeamMemberDTO getTeamMemberById(Long id) throws UserNotFoundException;


    List<TeamMemberDTO> getTeamMemberList();

    TeamMemberDTO createNewUser(TeamMember teamMember) throws UserAlreadyExistsException;

    String deleteUser(Long id) throws UserNotFoundException;


    TeamMemberDTO addToTeam(Long userId, Long teamId) throws UserNotFoundException, TeamNotFoundException;


    TeamMemberDTO removeFromTeam(Long userId) throws UserNotFoundException;

    TeamMemberDTO updateUser(TeamMember updatedTeamMember) throws UserNotFoundException;

    List<TeamDTO> getAdminTeams(Long id) throws UserNotFoundException;

    TeamMemberDTO getTeamMemberByEmail(String email) throws UserNotFoundException;
    
}
