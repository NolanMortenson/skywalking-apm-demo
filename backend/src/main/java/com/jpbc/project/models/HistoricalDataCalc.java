package com.jpbc.project.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDataCalc {
    private LocalDate sprintStart;
    private LocalDate sprintEnd;
    private int sprintNumber;
    private int workDays;
    private int storiesCompleted;
    private float sprintCycleTime;
    
}
