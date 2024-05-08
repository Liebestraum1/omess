package org.sixback.omess.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.model.entity.QMember;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final JPAQueryFactory jPAQueryFactory;

    @Transactional(readOnly = true)
    public List<Member> searchMember(Long memberId, String email, String nickname) {
        return jPAQueryFactory.selectFrom(QMember.member)
                .where(memberIdEqOrEmailContainsOrNicknameContainsCondition(memberId, email, nickname))
                .fetch();
    }

    private BooleanExpression memberIdEqOrEmailContainsOrNicknameContainsCondition(
            Long memberId, String email, String nickname) {
        BooleanExpression booleanExpression = memberIdEq(memberId);
        if (booleanExpression == null) {
            booleanExpression = QMember.member.id.gt(0);
        }
        return booleanExpression
                .and(emailContains(email))
                .and(nicknameContains(nickname));
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : QMember.member.id.eq(memberId);
    }

    private BooleanExpression emailContains(String email) {
        return email == null ? null : QMember.member.email.contains(email);
    }

    private BooleanExpression nicknameContains(String nickname) {
        return nickname == null ? null : QMember.member.nickname.contains(nickname);
    }
}
