package com.jpbc.project.controllers;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.exceptionHandler.TeamAlreadyExistsException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.models.Team;
import com.jpbc.project.services.TeamService;
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

@RestController
@RequestMapping("/api/v1/teams")
@CrossOrigin
@AllArgsConstructor
public class TeamController {

    private static final String TEAM_ALREADY_EXISTS = "Team name already exists. Please choose a unique name.";
    private static final String TEAM_NOT_FOUND = "Team Not Found: ID Does Not Match Or Does Not Exist";
    private final TeamService teamService;

    @ExceptionHandler(value = TeamAlreadyExistsException.class)
    public ResponseEntity handleTeamAlreadyExistsException(TeamAlreadyExistsException teamAlreadyExistsException) {
        return new ResponseEntity(TEAM_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TeamNotFoundException.class)
    public ResponseEntity handleTeamNotFoundException(TeamNotFoundException teamNotFoundException) {
        return new ResponseEntity(TEAM_NOT_FOUND, HttpStatus.CONFLICT);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@RequestBody Team newTeam) {
        return teamService.createNewTeam(newTeam);
    }

    @GetMapping("/{id}")
    public TeamDTO getTeam(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @DeleteMapping("/{id}/delete")
    public Team deleteTeam(@PathVariable Long id) {
        return teamService.deleteTeam(id);
    }

    @PutMapping
    public void updateTeam(@RequestBody Team updatedTeam) {
        teamService.updateTeam(updatedTeam);
    }

}
