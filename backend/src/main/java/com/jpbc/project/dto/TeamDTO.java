package com.jpbc.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {

    private Long id;
    private String teamName;
    private ProjectDTO project;
    private List<ShortTeamMemberDTO> memberList;
    private ShortTeamMemberDTO Admin;

}
