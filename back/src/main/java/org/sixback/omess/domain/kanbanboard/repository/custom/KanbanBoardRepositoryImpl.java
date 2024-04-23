package org.sixback.omess.domain.kanbanboard.repository.custom;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.QKanbanBoard;
import org.sixback.omess.domain.module.model.entity.QModule;
import org.sixback.omess.domain.project.model.entity.QProject;

public class KanbanBoardRepositoryImpl implements KanbanBoardCustomRepository{
    private final JPQLQueryFactory jpaQueryFactory;
    public KanbanBoardRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

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
