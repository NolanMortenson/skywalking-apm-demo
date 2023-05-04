package com.jpbc.project.repos;

import com.jpbc.project.models.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataRepo extends JpaRepository<HistoricalData, Long> {
    HistoricalData findTopByOrderByCreationDateDesc();

    List<HistoricalData> findByResolvedDateBetween(LocalDate startDate, LocalDate endDate);

    List<HistoricalData> findByProjectId(Long id);
}
