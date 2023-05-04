package com.jpbc.project.services;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ProjectResultDTO;
import com.jpbc.project.exceptionHandler.ProjectAlreadyExistsException;
import com.jpbc.project.exceptionHandler.ProjectNotFoundException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.libraries.CsvParser;
import com.jpbc.project.libraries.MonteCarlo;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.mappers.MapstructMapperImpl;
import com.jpbc.project.models.HistoricalData;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.SimulationResult;
import com.jpbc.project.models.Team;
import com.jpbc.project.repos.HistoricalDataRepo;
import com.jpbc.project.repos.ProjectRepo;
import com.jpbc.project.repos.SimulationResultRepo;
import com.jpbc.project.repos.TeamRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectServiceImplTest {

    @Mock
    ProjectService projectService;

    @Mock
    ProjectRepo projectRepo;

    @Mock
    HistoricalDataRepo historicalDataRepo;

    @Mock
    SimulationResultRepo simulationResultRepo;

    @Mock
    TeamRepo teamRepo;

    @Mock
    MonteCarlo monteCarlo;

    @Mock
    TeamServiceImpl teamService;

    @Mock
    CsvParser csvParser;

    MapstructMapper mapper = new MapstructMapperImpl();

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    Project project;

    ProjectDTO projectDTO;
    ProjectResultDTO projectResultDto;

    Team team;

    Long teamId = 1L;
    Long projectId = 1L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectService = new ProjectServiceImpl(historicalDataRepo, projectRepo, simulationResultRepo, teamRepo, teamService, mapper);

        project = new Project();
        project.setId(projectId);
        project.setProjectName("TEST PROJECT");
        project.setProjectSimulations(1000);
        project.setStoryCount(50);
        project.setForecastStartDate(new Date());
        team = new Team();
        team.setId(teamId);

        projectDTO = mapper.projectToDto(project);

        when(teamRepo.findTeamByProjectId(anyLong())).thenReturn(team);
        projectResultDto = mapper.projectToProjectResultDTO(project, teamRepo, simulationResultRepo);
    }

    @Test
    void createProject() {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "data.csv", MediaType.TEXT_PLAIN_VALUE, "1,2,3,4".getBytes());

        //parsing will not work due to file being null but we are not checking for
        // a working parser at the moment
        List<HistoricalData> parsedList = new ArrayList<>();
        HistoricalData thing = new HistoricalData();
        parsedList.add(thing);
        ArrayList<SimulationResult> points = new ArrayList<>();
        LocalDate date = LocalDate.of(2000, 1, 1);
        SimulationResult point1 = new SimulationResult(project, 3, 2, 0.25, 0.25, date);
        points.add(point1);

        //when
        when(teamRepo.existsById(anyLong())).thenReturn(true);
        when(projectRepo.existsByProjectName(anyString())).thenReturn(false);
        when(csvParser.read(file, project)).thenReturn(parsedList);
        when(monteCarlo.giveMeTheMonte(parsedList, project)).thenReturn(points);
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));

        projectService.createProject(file, project, teamId);

        //then
        verify(projectRepo, times(1)).save(project);
        verify(historicalDataRepo, times(1)).saveAll(anyList());
    }

    @Test
    void createProjectFailureTeamNotFound() {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "data.csv", MediaType.TEXT_PLAIN_VALUE, "1,2,3,4".getBytes());

        //parsing will not work due to file being null but we are not checking for
        // a working parser at the moment
        List<HistoricalData> parsedList = new ArrayList<>();
        HistoricalData thing = new HistoricalData();
        parsedList.add(thing);
        ArrayList<SimulationResult> points = new ArrayList<>();
        LocalDate date = LocalDate.of(2000, 1, 1);
        SimulationResult point1 = new SimulationResult(project, 3, 2, 0.25, 0.25, date);
        points.add(point1);

        //when
        when(teamRepo.existsById(anyLong())).thenReturn(false);
        when(projectRepo.existsByProjectName(anyString())).thenReturn(false);
        when(csvParser.read(file, project)).thenReturn(parsedList);
        when(monteCarlo.giveMeTheMonte(parsedList, project)).thenReturn(points);
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));

        //then
        Assertions.assertThrows(TeamNotFoundException.class, () -> projectService.createProject(file, project, teamId));
    }

    @Test
    void createProjectFailureProjectExists() {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "data.csv", MediaType.TEXT_PLAIN_VALUE, "1,2,3,4".getBytes());

        //parsing will not work due to file being null but we are not checking for
        // a working parser at the moment
        List<HistoricalData> parsedList = new ArrayList<>();
        HistoricalData thing = new HistoricalData();
        parsedList.add(thing);
        ArrayList<SimulationResult> points = new ArrayList<>();
        LocalDate date = LocalDate.of(2000, 1, 1);
        SimulationResult point1 = new SimulationResult(project, 3, 2, 0.25, 0.25, date);
        points.add(point1);

        //when
        when(teamRepo.existsById(anyLong())).thenReturn(true);
        when(projectRepo.existsByProjectName(anyString())).thenReturn(true);
        when(csvParser.read(file, project)).thenReturn(parsedList);
        when(monteCarlo.giveMeTheMonte(parsedList, project)).thenReturn(points);
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));

        //then
        Assertions.assertThrows(ProjectAlreadyExistsException.class, () -> projectService.createProject(file, project, teamId));
    }

    @Test
    void getProject() {

        // when
        when(projectRepo.existsById(anyLong())).thenReturn(true);
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // then
        Assertions.assertEquals(projectResultDto, projectService.getProject(projectId));
    }

    @Test
    void getProjectFailure() {
        //when
        when(projectRepo.existsById(anyLong())).thenReturn(false);
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // then
        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.getProject(1L));
    }

    @Test
    void updateProject() {
        // given
        Project projectBeforeUpdate = new Project();
        projectBeforeUpdate.setId(projectId);

        // when
        when(projectRepo.findById(any())).thenReturn(Optional.of(projectBeforeUpdate));
        when(projectRepo.existsByIdOrProjectName(anyLong(), anyString())).thenReturn(true);
        projectService.updateProject(project);

        // then
        Assertions.assertEquals(project, projectBeforeUpdate);
        verify(projectRepo, times(1)).save(project);
    }

    @Test
    void updateProjectFailure() {
        // given
        Project projectBeforeUpdate = new Project();
        projectBeforeUpdate.setId(projectId);

        // when
        when(projectRepo.findById(any())).thenReturn(Optional.of(projectBeforeUpdate));
        when(projectRepo.existsByIdOrProjectName(anyLong(), anyString())).thenReturn(false);

        // then
        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(project));
    }

    @Test
    void deleteProject() {
        //when
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(projectRepo.existsById(anyLong())).thenReturn(true);
        projectService.deleteProject(projectId);

        //then
        verify(projectRepo, times(1)).deleteById(anyLong());

    }

    @Test
    void deleteProjectFailure() {
        //when
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(projectRepo.existsById(anyLong())).thenReturn(false);

        //then
        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(1L));

    }
}