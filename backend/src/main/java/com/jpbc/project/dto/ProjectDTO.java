package com.jpbc.project.dto;

import com.jpbc.project.models.GraphPoints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long id;
    private Long adminId;
    private String projectName;
    private Date forecastStartDate;
    private Integer storyCount;
    private Integer sprintDuration;
    private Integer workDaysInSprint;
    private Date historicalDataStartDate;
    private Integer historicalDataFirstSprintId;
    private Integer sprintHistoryToEvaluate;
    private Integer projectSimulations;
    private Boolean isPublic;

}
