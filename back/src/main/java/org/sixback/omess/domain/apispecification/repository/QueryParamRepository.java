package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryParamRepository extends JpaRepository<QueryParam, Long> {
}
