package org.sixback.omess.domain.member.repository;

import org.sixback.omess.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
