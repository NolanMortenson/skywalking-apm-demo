package com.jpbc.project.controllers;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.exceptionHandler.UserAlreadyExistsException;
import com.jpbc.project.exceptionHandler.UserNotFoundException;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.services.TeamMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
@AllArgsConstructor
public class TeamMemberController {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String TEAM_NOT_FOUND = "Team Not Found: ID Does Not Match Or Exist";
    private static final String USER_EMAIL_IN_USE = "Email address already in use";
    private final TeamMemberService teamMemberService;

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
        return new ResponseEntity(USER_EMAIL_IN_USE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity(USER_NOT_FOUND, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TeamNotFoundException.class)
    public ResponseEntity handleTeamNotFoundException(TeamNotFoundException teamNotFoundException) {
        return new ResponseEntity(TEAM_NOT_FOUND, HttpStatus.CONFLICT);
    }

    @GetMapping
    public List<TeamMemberDTO> getAllUsers() {
        return teamMemberService.getTeamMemberList();
    }

    @GetMapping("/admins")
    public List<TeamMemberDTO> getAllAdmins() {
        return teamMemberService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public TeamMemberDTO getUserById(@PathVariable Long id) {
        return teamMemberService.getTeamMemberById(id);
    }

    @GetMapping("/admins/{id}/all-teams")
    public List<TeamDTO> getAdminTeams(@PathVariable Long id) {
        return teamMemberService.getAdminTeams(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) // Return 201 status code instead of 200
    public TeamMemberDTO createNewUser(@RequestBody TeamMember teamMember) {
        return teamMemberService.createNewUser(teamMember);
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        return teamMemberService.deleteUser(id);
    }

    // Update existing user with provided body
    @PutMapping
    public TeamMemberDTO updateUser(@RequestBody TeamMember updatedTeamMember) {
        // Update user in the db
        return teamMemberService.updateUser(updatedTeamMember);
    }

    @PutMapping("/{userId}/add-team/{teamId}")
    public TeamMemberDTO addUserToTeam(@PathVariable Long userId, @PathVariable Long teamId) {
        return teamMemberService.addToTeam(userId, teamId);
    }

    @PutMapping("/{userId}/remove-team")
    public TeamMemberDTO removeUserFromTeam(@PathVariable Long userId) {
        return teamMemberService.removeFromTeam(userId);
    }

    @GetMapping("/get/{email}")
    public TeamMemberDTO getUserByEmail(@PathVariable String email) {
        return teamMemberService.getTeamMemberByEmail(email);
    }

}