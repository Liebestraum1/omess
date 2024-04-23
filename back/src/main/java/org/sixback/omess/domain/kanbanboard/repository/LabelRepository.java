package org.sixback.omess.domain.kanbanboard.repository;

import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
