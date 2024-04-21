package org.sixback.omess.domain.project.repository;

import org.sixback.omess.domain.project.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
