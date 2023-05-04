package com.jpbc.project.services;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ProjectResultDTO;
import com.jpbc.project.exceptionHandler.ProjectAccessLockedException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyExistsException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyLinkedToTeamException;
import com.jpbc.project.exceptionHandler.ProjectNotFoundException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.models.Project;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface ProjectService {
    ProjectResultDTO createProject(MultipartFile file, Project project, Long teamId) throws ProjectNotFoundException, ProjectAlreadyLinkedToTeamException, TeamNotFoundException;

    ProjectResultDTO getProject(Long id) throws ProjectAccessLockedException;

    List<ProjectResultDTO> getProjectListByAdminId(Long id) throws ProjectNotFoundException;

    ProjectDTO getProjectDetails(Long id);

    Project updateProject(Project projectUpdate) throws ProjectNotFoundException, ProjectAlreadyExistsException;

    ProjectDTO updatePublic(Long id, Boolean isPublic) throws ProjectNotFoundException;

    ProjectResultDTO rerunSimulation(Long id, Date startDate, Integer storyCount) throws ProjectNotFoundException;

    void deleteProject(Long projectId) throws ProjectNotFoundException;

}
