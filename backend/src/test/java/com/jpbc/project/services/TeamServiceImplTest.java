package com.jpbc.project.services;

import com.jpbc.project.dto.ShortTeamMemberDTO;
import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.exceptionHandler.TeamAlreadyExistsException;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.mappers.MapstructMapperImpl;
import com.jpbc.project.models.Team;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    private final MapstructMapper mapper = new MapstructMapperImpl();
    List<Team> teamList;
    @Mock
    private TeamRepo teamRepo;

    @Mock
    private TeamMemberRepo teamMemberRepo;
    @InjectMocks
    private TeamServiceImpl teamServiceImpl;
    private Team team1;
    private Team team2;

    private TeamDTO teamDTO;


    @BeforeEach
    public void setUp() {
        teamServiceImpl = new TeamServiceImpl(teamRepo, teamMemberRepo, mapper);
        teamList = new ArrayList<>();
        team1 = new Team();
        team1.setId(1L);
        team2 = new Team();
        team2.setId(1L);
        teamList.add(team1);
        teamList.add(team2);
        teamDTO = new TeamDTO();
        teamDTO.setId(1L);
        List<ShortTeamMemberDTO> membersList = new ArrayList<>();
        teamDTO.setMemberList(membersList);
    }

    @AfterEach
    public void tearDown() {
        team1 = team2 = null;
        teamList = null;
    }

    @Test
    void createNewTeam() {
        //given
        team1.setTeamName("teamName");
        team2.setTeamName("teamName");
        //when
        when(teamRepo.save(team1)).thenReturn(team1);
        //then
        Assertions.assertEquals(team2, teamServiceImpl.createNewTeam(team1));
        verify(teamRepo, times(1)).save(team1);

    }

    @Test
    void createNewTeamFailure() {
        //given
        team1.setTeamName("name");
        team1.setId(1L);

        //when
        when(teamRepo.existsTeamByIdOrTeamNameIgnoreCase(anyLong(), anyString())).thenReturn(true);

        //then
        Assertions.assertThrows(TeamAlreadyExistsException.class, () -> teamServiceImpl.createNewTeam(team1));

    }

    @Test
    void getTeamById() {
        //when
        when(teamRepo.existsById(anyLong())).thenReturn(true);
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team1));
        //then
        Assertions.assertEquals(teamDTO, teamServiceImpl.getTeamById(1L));

    }

    @Test
    void getTeamByIdFailure() {
        //when
        when(teamRepo.existsById(anyLong())).thenReturn(false);
        //then
        Assertions.assertThrows(TeamNotFoundException.class, () -> teamServiceImpl.getTeamById(1L));

    }

    @Test
    void deleteTeam() {
        //when
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team1));
        teamServiceImpl.deleteTeam(1L);
        //then
        verify(teamRepo, times(1)).deleteById(team1.getId());
    }

    @Test
    void deleteTeamFailure() {
        //then
        Assertions.assertThrows(TeamNotFoundException.class, () -> teamServiceImpl.deleteTeam(1L));
    }

    @Test
    void updateTeam() {
        //given
        Team beforeUpdate = new Team();
        beforeUpdate.setId(1L);
        beforeUpdate.setTeamName("beforeTeam");
        Team afterUpdate = new Team("team");
        afterUpdate.setId(1L);
        //when
        when(teamRepo.existsTeamByIdOrTeamNameIgnoreCase(anyLong(), anyString())).thenReturn(true);
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(beforeUpdate));
        teamServiceImpl.updateTeam(afterUpdate);
        //then
        Assertions.assertEquals(afterUpdate, beforeUpdate);
        verify(teamRepo, times(1)).save(afterUpdate);

    }

    @Test
    void updateTeamFailure() {
        //given
        Team beforeUpdate = new Team();
        beforeUpdate.setId(1L);
        beforeUpdate.setTeamName("beforeTeam");
        Team afterUpdate = new Team("team");
        afterUpdate.setId(1L);

        //then
        Assertions.assertThrows(TeamNotFoundException.class, () -> teamServiceImpl.updateTeam(beforeUpdate));

    }
}