package org.sixback.omess.domain.member.repository;

import org.sixback.omess.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsMemberByEmail(String email);

    boolean existsMemberByNickname(String nickname);
}
