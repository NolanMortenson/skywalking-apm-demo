package com.jpbc.project.mappers;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ProjectResultDTO;
import com.jpbc.project.dto.ShortTeamDTO;
import com.jpbc.project.dto.ShortTeamMemberDTO;
import com.jpbc.project.dto.SimulationResultDTO;
import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.SimulationResult;
import com.jpbc.project.models.Team;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.SimulationResultRepo;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapstructMapper {
    @Mapping(target = "team", expression = "java(teamMember.getTeam() != null ? teamToShortDTO(teamMember.getTeam()) : null)")
    TeamMemberDTO teamMemberToDto(TeamMember teamMember);

    ShortTeamMemberDTO shortTeamMemberDTO(TeamMember teamMember);

    List<TeamMemberDTO> teamMemberListToDto(List<TeamMember> teamMemberList);

    List<ShortTeamMemberDTO> teamMemberListToShortDto(List<TeamMember> teamMemberList);

    @Mapping(target = "adminId", expression = "java(project.getAdmin() != null ? project.getAdmin().getId() : null)")
    ProjectDTO projectToDto(Project project);

    @Mapping(target = "startDate", source = "forecastStartDate")
    @Mapping(target = "projectId", source = "id")
    @Mapping(target = "numOfStories", source = "storyCount")
    @Mapping(target = "teamId", ignore = true)
    @Mapping(target = "teamName", ignore = true)
    @Mapping(target = "graphData", ignore = true)
    ProjectResultDTO projectToProjectResultDTO(Project project, @Context TeamRepo teamRepo, @Context SimulationResultRepo simulationResultRepo);

    @AfterMapping
    default void projectToProjectResultDTO(@MappingTarget ProjectResultDTO projectResultDTO, Project project, @Context TeamRepo teamRepo, @Context SimulationResultRepo simulationResultRepo) {
        Team team = teamRepo.findTeamByProjectId(project.getId());
        projectResultDTO.setTeamName(team.getTeamName());
        projectResultDTO.setTeamId(team.getId());
        projectResultDTO.setGraphData(this.simulationResultListToDto(simulationResultRepo.findSimulationResultsByProjectId(project.getId())));
    }

    List<ProjectResultDTO> projectListToProjectResultDTOList(List<Project> projectList, @Context TeamRepo teamRepo, @Context SimulationResultRepo simulationResultRepo);

    List<TeamMemberDTO> adminListToDTO(List<TeamMember> adminList);

    @Mapping(target = "memberList", ignore = true)
    TeamDTO teamToDto(Team team, @Context TeamMemberRepo teamMemberRepo);

    List<TeamDTO> teamListToDTO(List<Team> teamList, @Context TeamMemberRepo teamMemberRepo);

    @Mapping(target = "admin", expression = "java(team.getAdmin() != null ? shortTeamMemberDTO(team.getAdmin()) : null)")
    ShortTeamDTO teamToShortDTO(Team team);


    List<ShortTeamDTO>shortTeamsToDto(List<Team> teamList);

    @AfterMapping
    default void teamToDto(@MappingTarget TeamDTO teamDTO, Team team, @Context TeamMemberRepo teamMemberRepo) {
        teamDTO.setMemberList(teamMemberListToShortDto(teamMemberRepo.findTeamMembersByTeamId(team.getId())));
        teamDTO.setAdmin(shortTeamMemberDTO(team.getAdmin()));
    }

    SimulationResultDTO simulationResultToDto(SimulationResult result);

    List<SimulationResultDTO> simulationResultListToDto(List<SimulationResult> results);

}
