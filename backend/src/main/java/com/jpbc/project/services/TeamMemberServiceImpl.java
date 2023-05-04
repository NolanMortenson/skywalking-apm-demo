package com.jpbc.project.services;

import com.jpbc.project.dto.TeamDTO;
import com.jpbc.project.dto.TeamMemberDTO;
import com.jpbc.project.exceptionHandler.TeamNotFoundException;
import com.jpbc.project.exceptionHandler.UserAlreadyExistsException;
import com.jpbc.project.exceptionHandler.UserNotFoundException;
import com.jpbc.project.mappers.MapstructMapper;
import com.jpbc.project.models.Team;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.repos.TeamRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService, UserDetailsService {

    private final TeamMemberRepo teamMemberRepo;
    private final MapstructMapper mapper;
    private final TeamRepo teamRepo;
    private PasswordEncoder passwordEncoder;


    @Override
    public List<TeamMemberDTO> getAllAdmins() {
        return mapper.adminListToDTO(teamMemberRepo.findAll());
    }

    @Override
    public TeamMemberDTO getTeamMemberById(Long id) throws UserNotFoundException {
        TeamMemberDTO teamMemberDTO;

        if (teamMemberRepo.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        } else {
            teamMemberDTO = mapper.teamMemberToDto(teamMemberRepo.findById(id).orElse(null));
        }

        return teamMemberDTO;
    }

    @Override
    public List<TeamMemberDTO> getTeamMemberList() {
        return mapper.teamMemberListToDto(teamMemberRepo.findAll());
    }

    @Override
    public TeamMemberDTO createNewUser(TeamMember teamMember) throws UserAlreadyExistsException {


        Optional<TeamMember> usernameEntry = teamMemberRepo.findByEmail(teamMember.getEmail());

        if (usernameEntry.isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            teamMember.setPassword(passwordEncoder.encode(teamMember.getPassword()));
            return mapper.teamMemberToDto(teamMemberRepo.save(teamMember));
        }
    }


    @Override
    public String deleteUser(Long id) throws UserNotFoundException {
        // Make sure user exists
        if (!teamMemberRepo.existsById(id)) {
            throw new UserNotFoundException();
        }
        TeamMember userToDelete = teamMemberRepo.findById(id).orElse(null);

        teamMemberRepo.deleteById(id);
        return "Successfully deleted " +
                userToDelete.getFirstName() +
                " " +
                userToDelete.getLastName();
    }

    @Override
    public TeamMemberDTO updateUser(TeamMember updatedTeamMember) throws UserNotFoundException {
        if (!teamMemberRepo.existsById(updatedTeamMember.getId()) || updatedTeamMember == null) {
            throw new UserNotFoundException();
        }
        TeamMember teamMemberEdit = teamMemberRepo.findById(updatedTeamMember.getId()).orElse(null);

        teamMemberEdit.setFirstName(updatedTeamMember.getFirstName());
        teamMemberEdit.setLastName(updatedTeamMember.getLastName());
        teamMemberEdit.setEmail(updatedTeamMember.getEmail());
        teamMemberEdit.setTeam(updatedTeamMember.getTeam());
        //not updating password may need to add later

        return mapper.teamMemberToDto(teamMemberRepo.save(teamMemberEdit));
    }

    @Override
    public List<TeamDTO> getAdminTeams(Long id) throws UserNotFoundException {
        if (teamMemberRepo.existsById(id)) {
            return mapper.teamListToDTO(teamRepo.findByAdminId(id), teamMemberRepo);
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public TeamMemberDTO addToTeam(Long userId, Long teamId) throws UserNotFoundException, TeamNotFoundException {
        TeamMember teamMember = teamMemberRepo.findById(userId).orElse(null);
        Team team = teamRepo.findById(teamId).orElse(null);

        if (!teamMemberRepo.existsById(userId)) {
            throw new UserNotFoundException();
        }

        if (!teamRepo.existsById(teamId)) {
            throw new TeamNotFoundException();
        }

        teamMember.setTeam(team);
        return mapper.teamMemberToDto(teamMemberRepo.save(teamMember));
    }

    @Override
    public TeamMemberDTO removeFromTeam(Long userId) throws UserNotFoundException {
        TeamMember teamMember = teamMemberRepo.findById(userId).orElse(null);

        if (!teamMemberRepo.existsById(userId)) {
            throw new UserNotFoundException();
        }

        teamMember.setTeam(null);
        return mapper.teamMemberToDto(teamMemberRepo.save(teamMember));
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException, UserNotFoundException {
        TeamMember teamMember = teamMemberRepo.findByEmail(email).orElse(null);
        if (!teamMemberRepo.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        return new org.springframework.security.core.userdetails.User(teamMember.getEmail(), teamMember.getPassword(), teamMember.getAuthorities());

    }

    @Override
    public TeamMemberDTO getTeamMemberByEmail(String email) throws UserNotFoundException {
        if (teamMemberRepo.existsByEmail(email)) {
            return mapper.teamMemberToDto(teamMemberRepo.findByEmail(email).orElse(null));
        } else {
            throw new UserNotFoundException();
        }

    }


}
