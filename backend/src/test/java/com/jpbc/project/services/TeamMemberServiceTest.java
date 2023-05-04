package com.jpbc.project.services;

import com.jpbc.project.dto.ProjectDTO;
import com.jpbc.project.dto.ShortTeamDTO;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.exceptionHandler.UserAlreadyExistsException;
import com.jpbc.project.exceptionHandler.UserNotFoundException;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.mappers.MapstructMapperImpl;
import com.jpbc.project.models.GraphPoints;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.Role;
import com.jpbc.project.models.Team;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class TeamMemberServiceTest {

    private final MapstructMapper mapper = new MapstructMapperImpl();
    @InjectMocks
    TeamMemberServiceImpl teamMemberService;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TeamMemberRepo teamMemberRepo;

    @Mock
    TeamRepo teamRepo;

    TeamMember user;
    Project project;
    Team team;

    ProjectDTO projectDTO;
    TeamMemberDTO teamMemberDTO;
    TeamMemberDTO adminDTO;


    Team adminTeam;

    Long userId = 1L;

    List<GraphPoints> points;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        points = null;

        teamMemberService = new TeamMemberServiceImpl(teamMemberRepo, mapper, teamRepo, passwordEncoder);

        team = new Team("team");
        team.setId(1L);

        ShortTeamDTO team2 = new ShortTeamDTO(team.getId(), "team", null, null);

        adminTeam = new Team("adminTeam");

        user = new TeamMember("Monte", "Carlo", "mc@test.com", "password", null, Role.USER, team);
        user.setId(userId);
        adminDTO = new TeamMemberDTO(userId, "testAdmin", "lastname", "email", null, team2, Role.ADMIN);
        adminDTO.setId(1L);
        project = new Project(1L, null, "testProject", null, 0, 0, 0,
                null, 0, 0, 0, false);

        project.setAdmin(new TeamMember(userId, "testAdmin", "lastname", "email", "password", null, Role.ADMIN, null));
        team.setProject(project);
        user.setTeam(team);
        user.setRole(Role.USER);

        teamMemberDTO = new TeamMemberDTO(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getLastLogin(), mapper.teamToShortDTO(user.getTeam()), user.getRole());

        projectDTO = new ProjectDTO(project.getId(), adminDTO.getId(), "testProject", null, 0, 0, 0,
                null, 0, 0, 0, false);

    }

    @Test
    void getTeamMemberById() {
        //when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));

        //then
        assertEquals(teamMemberDTO, teamMemberService.getTeamMemberById(userId));

    }

    @Test
    void getTeamMemberByIdFailure() {
        //then
        Assertions.assertThrows(UserNotFoundException.class, () -> teamMemberService.getTeamMemberById(1L));
    }

    @Test
    void DTOConversionFromTeamMembersToTeamMemberDTOs() {
        //given
        List<TeamMember> teamMembers = new ArrayList<>();

        teamMembers.add(new TeamMember("John", "Smith", "jsmith@test.com", "password", null, user.getRole(), team));
        teamMembers.add(new TeamMember("Jane", "Smith", "jsmith@test.com", "password", null, user.getRole(), team));
        teamMembers.add(new TeamMember("Jimmy", "Smith", "jsmith@test.com", "password", null, user.getRole(), team));

        //needed to set teamMember's team to prep for conversion
        for (TeamMember teamMember : teamMembers) {
            teamMember.setTeam(team);
        }

        List<TeamMemberDTO> teamMemberDTOS = new ArrayList<>();
        teamMemberDTOS.add(new TeamMemberDTO(teamMembers.get(0).getId(), "John", "Smith", "jsmith@test.com", null, mapper.teamToShortDTO(team), user.getRole()));
        teamMemberDTOS.add(new TeamMemberDTO(teamMembers.get(1).getId(), "Jane", "Smith", "jsmith@test.com", null, mapper.teamToShortDTO(team), user.getRole()));
        teamMemberDTOS.add(new TeamMemberDTO(teamMembers.get(2).getId(), "Jimmy", "Smith", "jsmith@test.com", null, mapper.teamToShortDTO(team), user.getRole()));

        //when
        when(teamMemberRepo.findAll()).thenReturn(teamMembers);

        //then
        assertEquals(teamMemberDTOS, teamMemberService.getTeamMemberList());
    }

    @Test
    void createNewUser() {
        //when
        when(teamMemberRepo.save(user)).thenReturn(user);
        //then
        Assertions.assertEquals(teamMemberDTO, teamMemberService.createNewUser(user));
        verify(teamMemberRepo, times(1)).save(user);
    }

    @Test
    void createNewUserFailure() {
        //when
        when(teamMemberRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        //then
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> teamMemberService.createNewUser(user));
    }

    @Test
    void deleteUser() {
        //when
        when(teamMemberRepo.findById(userId)).thenReturn(Optional.of(user));
        when(teamMemberRepo.existsById(userId)).thenReturn(true);
        teamMemberService.deleteUser(userId);

        //then
        verify(teamMemberRepo, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserFailure() {
        //when
        when(teamMemberRepo.existsById(userId)).thenReturn(false);

        //then
        Assertions.assertThrows(UserNotFoundException.class, () -> teamMemberService.deleteUser(1L));
    }

    @Test
    void updateUser() {
        //given
        TeamMember userBeforeUpdate = new TeamMember();
        userBeforeUpdate.setId(userId);
        TeamMember userAfterUpdate = new TeamMember("john", "smith", "1234@bah.com", null, null, null, team);
        userAfterUpdate.setId(userId);

        //when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(userBeforeUpdate));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(true);
        //this is updating the empty user to the updated version
        teamMemberService.updateUser(userAfterUpdate);

        //asserting to make sure update occurred during service call
        assertEquals(userAfterUpdate, userBeforeUpdate);
        verify(teamMemberRepo, times(1)).save(userAfterUpdate);
    }

    @Test
    void updateUserFailure() {
        //given
        TeamMember userBeforeUpdate = new TeamMember();
        userBeforeUpdate.setId(userId);
        TeamMember userAfterUpdate = new TeamMember("john", "smith", "1234@bah.com", null, null, null, team);
        userAfterUpdate.setId(userId);

        //when
        when(teamMemberRepo.existsById(anyLong())).thenReturn(false);

        //then
        Assertions.assertThrows(UserNotFoundException.class, () -> teamMemberService.updateUser(user));
    }

    @Test
    void addToTeam() {
        // given
        user.setTeam(null);

        // when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(true);
        when(teamRepo.existsById(anyLong())).thenReturn(true);

        teamMemberService.addToTeam(user.getId(), team.getId());

        // then
        assertNotNull(user.getTeam());
    }

    @Test
    void addToTeamFailureUserNotFound() {
        // given
        user.setTeam(null);

        // when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(false);
        when(teamRepo.existsById(anyLong())).thenReturn(true);


        // then
        Assertions.assertThrows(UserNotFoundException.class, () -> teamMemberService.addToTeam(user.getId(), team.getId()));
    }

    @Test
    void addToTeamFailureTeamNotFound() {
        // given
        user.setTeam(null);

        // when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(true);
        when(teamRepo.existsById(anyLong())).thenReturn(false);


        // then
        Assertions.assertThrows(TeamNotFoundException.class, () -> teamMemberService.addToTeam(user.getId(), team.getId()));
    }

    @Test
    void removeFromTeam() {
        // when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(true);
        teamMemberService.removeFromTeam(user.getId());

        // then
        assertNull(user.getTeam());
    }

    @Test
    void removeFromTeamFailure() {
        // when
        when(teamMemberRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamMemberRepo.existsById(anyLong())).thenReturn(false);

        // then
        Assertions.assertThrows(UserNotFoundException.class, () -> teamMemberService.removeFromTeam(user.getId()));
    }
}