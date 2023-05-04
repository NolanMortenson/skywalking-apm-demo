package com.jpbc.project.repos;

import com.jpbc.project.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findProjectsByAdmin_Id(Long id);

    boolean existsByIdOrProjectName(Long id, String name);

    boolean existsByProjectName(String name);
}
