package com.jpbc.project.controllers;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ProjectResultDTO;
import com.jpbc.project.exceptionHandler.ProjectAccessLockedException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyExistsException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyLinkedToTeamException;
import com.jpbc.project.exceptionHandler.ProjectNotFoundException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.models.Project;
import com.jpbc.project.services.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin
@AllArgsConstructor
public class ProjectController {

    private static final String PROJECT_ALREADY_EXISTS = "Project Name Already Exists";
    private static final String PROJECT_NOT_FOUND = "Project Not Found: ID Does Not Match Or Exist";
    private static final String PROJECT_LINKED_TO_TEAM = "Project Is Already Linked To Team";
    private static final String PROJECT_ACCESS = "Project Access Is Locked";
    private static final String TEAM_NOT_FOUND = "Team Not Found: ID Does Not Match Or Does Not Exist";
    private final ProjectService projectService;

    @ExceptionHandler(value = ProjectAlreadyExistsException.class)
    public ResponseEntity handleProjectAlreadyExistsException(ProjectAlreadyExistsException projectAlreadyExistsException) {
        return new ResponseEntity(PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ProjectNotFoundException.class)
    public ResponseEntity handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
        return new ResponseEntity(PROJECT_NOT_FOUND, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ProjectAlreadyLinkedToTeamException.class)
    public ResponseEntity handleProjectAlreadyLinkedToTeamException(ProjectAlreadyLinkedToTeamException projectAlreadyLinkedToTeamException) {
        return new ResponseEntity(PROJECT_LINKED_TO_TEAM, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ProjectAccessLockedException.class)
    public ResponseEntity handleProjectAccessLockedException(ProjectAccessLockedException projectAccessLockedException) {
        return new ResponseEntity(PROJECT_ACCESS, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = TeamNotFoundException.class)
    public ResponseEntity handleTeamNotFoundException(TeamNotFoundException teamNotFoundException) {
        return new ResponseEntity(TEAM_NOT_FOUND, HttpStatus.CONFLICT);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResultDTO createProject(@RequestPart("file") MultipartFile file,
                                          @RequestPart("project") Project project,
                                          @RequestPart("team_id") Long teamId) {
        // TODO: accept req body with project params
        // TODO: call the service to create project & run simulations
        return projectService.createProject(file, project, teamId);
    }

    @GetMapping("/{id}")
    public ProjectResultDTO getProject(@PathVariable Long id) {
        // TODO: Call Service to get project by project id
        return projectService.getProject(id);
    }

    @GetMapping("/{id}/details")
    public ProjectDTO getProjectDetails(@PathVariable Long id) {
        return projectService.getProjectDetails(id);
    }

    @GetMapping("/admin/{id}")
    public List<ProjectResultDTO> getProjectListByAdminId(@PathVariable Long id) {
        return projectService.getProjectListByAdminId(id);
    }

    @PutMapping
    public Project updateProject(@RequestBody Project updatedProject) {
        // TODO: Call Service to update project with req body info
        // TODO: run simulations
        return projectService.updateProject(updatedProject);
    }

    @PutMapping("/{projectId}/{isPublic}/guest")
    public ProjectDTO updatePublic(@PathVariable Long projectId, @PathVariable Boolean isPublic) {
        return projectService.updatePublic(projectId, isPublic);
    }

    @DeleteMapping("/{projectId}/delete")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

    @PutMapping("/{projectId}/simulate")
    public ProjectResultDTO rerunSimulation(@PathVariable Long projectId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date, @RequestParam Integer story_count) {
        return projectService.rerunSimulation(projectId, start_date, story_count);
    }
}
