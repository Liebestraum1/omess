package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.RequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestHeaderRepository extends JpaRepository<RequestHeader, Long> {
}
