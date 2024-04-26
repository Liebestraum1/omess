package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiSpecificationRepository extends JpaRepository<ApiSpecification, Long> {
    Optional<ApiSpecification> findByProjectId(Long projectId);

    Optional<ApiSpecification> findByPath(String path);
}
