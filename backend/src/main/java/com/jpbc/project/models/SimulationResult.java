package com.jpbc.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    private Integer numberOfSprints;
    private Integer count;
    private Double partialSuccessRate;
    private Double cumulativeSuccessRate;
    private LocalDate endDate;

    public SimulationResult(Project project, Integer numberOfSprints, Integer count, Double partialSuccessRate, Double cumulativeSuccessRate, LocalDate endDate) {
        this.project = project;
        this.numberOfSprints = numberOfSprints;
        this.count = count;
        this.partialSuccessRate = partialSuccessRate;
        this.cumulativeSuccessRate = cumulativeSuccessRate;
        this.endDate = endDate;
    }
}
