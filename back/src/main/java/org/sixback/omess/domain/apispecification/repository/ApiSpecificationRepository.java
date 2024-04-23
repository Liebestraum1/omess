package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiSpecificationRepository extends JpaRepository<ApiSpecification, Long> {
}
