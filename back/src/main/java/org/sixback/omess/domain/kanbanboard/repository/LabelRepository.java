package org.sixback.omess.domain.kanbanboard.repository;

import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("select l from Label l where l.path like :kPath")
    List<Label> findAllByPath(String kPath);
}
