package org.sixback.omess.domain.kanbanboard.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
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
                .on(qMember.id.eq(qIssue.member.id))
                .where(qIssue.kanbanBoard.id.eq(moduleId))
                .fetch();
    }
}
