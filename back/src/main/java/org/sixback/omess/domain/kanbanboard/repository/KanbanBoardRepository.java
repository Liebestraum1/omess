package org.sixback.omess.domain.kanbanboard.repository;

import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.custom.KanbanBoardCustomRepository;
import org.sixback.omess.domain.module.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanBoardRepository extends JpaRepository<KanbanBoard, Long>, KanbanBoardCustomRepository {
}
