package com.jpbc.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortTeamDTO {
    private Long id;
    private String teamName;
    private ProjectDTO project;
    private ShortTeamMemberDTO Admin;
}
