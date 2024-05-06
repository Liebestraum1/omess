package com.sixback.omesschat.domain.member.repository;

import com.sixback.omesschat.domain.member.model.entity.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

    Mono<Member> findByEmail(String email);
}
