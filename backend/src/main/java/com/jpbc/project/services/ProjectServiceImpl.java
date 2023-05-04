package com.jpbc.project.services;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ProjectResultDTO;
import com.jpbc.project.exceptionHandler.ProjectAccessLockedException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyExistsException;
import com.jpbc.project.exceptionHandler.ProjectAlreadyLinkedToTeamException;
import com.jpbc.project.exceptionHandler.ProjectNotFoundException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.libraries.CsvParser;
import com.jpbc.project.libraries.MonteCarlo;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.models.HistoricalData;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.SimulationResult;
import com.jpbc.project.models.Team;
import com.jpbc.project.repos.HistoricalDataRepo;
import com.jpbc.project.repos.ProjectRepo;
import com.jpbc.project.repos.SimulationResultRepo;
import com.jpbc.project.repos.TeamRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.jpbc.project.security.SecurityUtil.checkAuthenticated;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {


    private final HistoricalDataRepo historicalDataRepo;
    private final ProjectRepo projectRepo;
    private final SimulationResultRepo simulationResultRepo;
    private final TeamRepo teamRepo;
    private final TeamServiceImpl teamService;
    private final MonteCarlo monteCarlo = new MonteCarlo();
    private final MapstructMapper mapper;

    @Override
    public ProjectResultDTO getProject(Long id) throws ProjectAccessLockedException, ProjectNotFoundException {
        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException();
        }
        Project project = projectRepo.findById(id).orElse(null);

        assert project != null;
        boolean isPublic = project.getIsPublic();
        boolean isAuthenticated = checkAuthenticated();

        if (isPublic || isAuthenticated) {
            return mapper.projectToProjectResultDTO(project, teamRepo, simulationResultRepo);
        }

        throw new ProjectAccessLockedException();
    }

    @Override
    public ProjectDTO getProjectDetails(Long id) {
        Project project = projectRepo.findById(id).orElse(null);
        return mapper.projectToDto(project);
    }

    @Override
    public List<ProjectResultDTO> getProjectListByAdminId(Long id) throws ProjectNotFoundException {

        return mapper.projectListToProjectResultDTOList(projectRepo.findProjectsByAdmin_Id(id), teamRepo,
                simulationResultRepo);

    }

    @Override
    public Project updateProject(Project projectUpdate) throws ProjectNotFoundException, ProjectAlreadyExistsException {

        if (!projectRepo.existsByIdOrProjectName(projectUpdate.getId(), projectUpdate.getProjectName())) {
            throw new ProjectNotFoundException();
        }
        Project project = projectRepo.findById(projectUpdate.getId()).orElse(null);

        if (projectRepo.existsByProjectName(projectUpdate.getProjectName())) {
            assert project != null;
            if (!Objects.equals(project.getProjectName(), projectUpdate.getProjectName())) {
                throw new ProjectAlreadyExistsException();
            }
        }

        project.setProjectName(projectUpdate.getProjectName());
        project.setProjectSimulations(projectUpdate.getProjectSimulations());
        project.setId(projectUpdate.getId());
        project.setAdmin(projectUpdate.getAdmin());
        project.setSprintDuration(projectUpdate.getSprintDuration());
        project.setForecastStartDate(projectUpdate.getForecastStartDate());
        project.setHistoricalDataFirstSprintId(projectUpdate.getHistoricalDataFirstSprintId());
        project.setHistoricalDataStartDate(projectUpdate.getHistoricalDataStartDate());
        project.setSprintHistoryToEvaluate(projectUpdate.getSprintHistoryToEvaluate());
        project.setStoryCount(projectUpdate.getStoryCount());
        project.setWorkDaysInSprint(projectUpdate.getWorkDaysInSprint());
        project.setProjectSimulations(projectUpdate.getProjectSimulations());
        project.setIsPublic(projectUpdate.getIsPublic());

        projectRepo.save(project);

        //TODO: Run Simulation again
        return project;
    }

    @Override
    public ProjectDTO updatePublic(Long id, Boolean isPublic) throws ProjectNotFoundException {
        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException();
        }

        Project project = projectRepo.findById(id).orElse(null);
        project.setIsPublic(isPublic);

        return mapper.projectToDto(projectRepo.save(project));
    }

    @Override
    @Transactional
    public ProjectResultDTO rerunSimulation(Long id, Date startDate, Integer storyCount) throws ProjectNotFoundException {


        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException();
        }
        Project project = projectRepo.findById(id).orElse(null);

        project.setStoryCount(storyCount);
        project.setForecastStartDate(startDate);
        projectRepo.save(project);

        List<HistoricalData> parsedList = historicalDataRepo.findByProjectId(id);

        simulationResultRepo.deleteAllByProjectId(id);
        List<SimulationResult> simulationResults = monteCarlo.giveMeTheMonte(parsedList, project);
        simulationResultRepo.saveAll(simulationResults);

        return mapper.projectToProjectResultDTO(project, teamRepo, simulationResultRepo);
    }

    @Override
    public void deleteProject(Long projectId) throws ProjectNotFoundException {
        Team team = teamRepo.findTeamByProjectId(projectId);
        if (!projectRepo.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }

        if (team != null) {
            team.setProject(null);
        }
        //search for project from simulation result and detach
        List<SimulationResult> simulationResults = simulationResultRepo.findSimulationResultsByProjectId(projectId);
        if (simulationResults != null) {
            for (SimulationResult result : simulationResults) {
                result.setProject(null);
            }
        }
        projectRepo.deleteById(projectId);
    }

    @Override
    public ProjectResultDTO createProject(MultipartFile file, Project project, Long teamId) throws ProjectNotFoundException, ProjectAlreadyLinkedToTeamException, TeamNotFoundException {
        if (!teamRepo.existsById(teamId)) {
            throw new TeamNotFoundException();
        }

        Team team = teamRepo.findById(teamId).orElse(null);

        if (projectRepo.existsByProjectName(project.getProjectName())) {
            throw new ProjectAlreadyExistsException();
        }

        assert team != null;
        if (team.getProject() != null) {
            throw new ProjectAlreadyLinkedToTeamException();
        }

        projectRepo.save(project);

        // Attach project to team
        team.setProject(project);
        teamService.updateTeam(team);

        CsvParser csvParser = new CsvParser();
        // send file to histData helper fn and retrieve list histData objects
        List<HistoricalData> parsedList = historicalDataRepo.saveAll(csvParser.read(file, project));

        List<SimulationResult> simulationResults = monteCarlo.giveMeTheMonte(parsedList, project);

        simulationResultRepo.saveAll(simulationResults);

        return mapper.projectToProjectResultDTO(project, teamRepo, simulationResultRepo);
    }
}
