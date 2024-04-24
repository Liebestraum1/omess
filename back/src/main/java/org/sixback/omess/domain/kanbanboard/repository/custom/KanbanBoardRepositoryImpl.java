package org.sixback.omess.domain.kanbanboard.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.QKanbanBoard;
import org.sixback.omess.domain.module.model.entity.QModule;
import org.sixback.omess.domain.project.model.entity.QProject;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class KanbanBoardRepositoryImpl implements KanbanBoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    private final QKanbanBoard qKanbanBoard = QKanbanBoard.kanbanBoard;
    private final QProject qProject = QProject.project;
    private final QModule qModule = QModule.module;

    @Override
    public KanbanBoard findByIdAndProjectId(Long projectId, Long moduleId) {
        return jpaQueryFactory
                .select(qKanbanBoard)
                .from(qKanbanBoard)
                .where(qKanbanBoard.id.eq(moduleId), qKanbanBoard.project.id.eq(projectId))
                .fetchOne();
    }
}
