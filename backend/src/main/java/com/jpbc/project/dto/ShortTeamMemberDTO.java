package com.jpbc.project.dto;

import com.jpbc.project.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortTeamMemberDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
