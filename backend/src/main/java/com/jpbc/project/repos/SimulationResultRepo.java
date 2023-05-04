package com.jpbc.project.repos;

import com.jpbc.project.models.SimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationResultRepo extends JpaRepository<SimulationResult, Long> {
    List<SimulationResult> findSimulationResultsByProjectId(Long id);

    void deleteAllByProjectId(Long id);
}
