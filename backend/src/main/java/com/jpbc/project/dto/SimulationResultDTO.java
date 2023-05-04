package com.jpbc.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResultDTO {
    private Integer numberOfSprints;
    private Integer count;
    private Double partialSuccessRate;
    private Double cumulativeSuccessRate;
    private LocalDate endDate;
}
