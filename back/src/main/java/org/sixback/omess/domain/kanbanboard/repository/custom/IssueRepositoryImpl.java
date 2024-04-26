package org.sixback.omess.domain.kanbanboard.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.sixback.omess.domain.kanbanboard.model.entity.QIssue;
import org.sixback.omess.domain.kanbanboard.model.entity.QLabel;
import org.sixback.omess.domain.member.model.entity.QMember;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QIssue qIssue = QIssue.issue;
    private final QLabel qLabel = QLabel.label;
    private final QMember qMember = QMember.member;
    @Override
    public List<Issue> findByModuleId(Long moduleId) {
        return jpaQueryFactory
                .select(qIssue)
                .from(qIssue)
                .leftJoin(qLabel)
                .on(qLabel.id.eq(qIssue.label.id))
                .leftJoin(qMember)
                .on(qMember.id.eq(qIssue.charger.id))
                .where(qIssue.kanbanBoard.id.eq(moduleId))
                .fetch();
    }

    @Override
    public List<Issue> getIssues(String path, Long chargerId, Long labelId, Integer importance) {
        return jpaQueryFactory
                .select(qIssue)
                .from(qIssue)
                .leftJoin(qLabel)
                .on(qLabel.id.eq(qIssue.label.id))
                .leftJoin(qMember)
                .on(qMember.id.eq(qIssue.charger.id))
                .where(qIssue.path.like(path + "%"), eqCharger(chargerId), eqLabelId(labelId), eqImportance(importance))
                .fetch();
    }

    @Override
    public void updateIssues(Long labelId) {
        jpaQueryFactory
                .update(qIssue)
                .set(qIssue.label, (Label) null)
                .where(qIssue.label.id.eq(labelId))
                .execute();
    }

    private BooleanExpression eqCharger(Long chargerId){
        return chargerId != null ? qIssue.charger.id.eq(chargerId) : null;
    }

    private BooleanExpression eqLabelId(Long labelId){
        return labelId != null ? qIssue.label.id.eq(labelId) : null;
    }

    private BooleanExpression eqImportance(Integer importance){
        return importance != null ? qIssue.importance.eq(importance) : null;
    }
}
