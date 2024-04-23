package org.sixback.omess.domain.kanbanboard.repository.custom;

import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;

public interface KanbanBoardCustomRepository {
    KanbanBoard findByIdAndProjectId(Long projectId, Long moduleId);
}
