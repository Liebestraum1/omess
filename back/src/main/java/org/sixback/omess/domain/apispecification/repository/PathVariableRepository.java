package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.PathVariable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathVariableRepository extends JpaRepository<PathVariable, Long> {
}
