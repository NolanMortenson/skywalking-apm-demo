package com.jpbc.project.dto;

import com.jpbc.project.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp lastLogin;
    private ShortTeamDTO team;
    private Role role;

}

