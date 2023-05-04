package com.jpbc.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResultDTO {
    Long projectId;
    String projectName;
    Long teamId;
    String teamName;
    Date startDate;
    Integer numOfStories;
    Boolean isPublic;
    List<SimulationResultDTO> graphData;
}
