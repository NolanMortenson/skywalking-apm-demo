package com.jpbc.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private TeamMember admin;

    private String projectName;
    private Date forecastStartDate;
    private Integer storyCount;
    private Integer sprintDuration = 14;
    private Integer workDaysInSprint = 10;
    private LocalDate historicalDataStartDate;
    private Integer historicalDataFirstSprintId;
    private Integer sprintHistoryToEvaluate = 15;
    private Integer projectSimulations = 1000;
    private Boolean isPublic = false;

}
