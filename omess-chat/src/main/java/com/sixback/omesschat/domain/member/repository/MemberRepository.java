package com.sixback.omesschat.domain.member.repository;

import com.sixback.omesschat.domain.member.model.entity.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
}
