package com.sixback.omesschat.domain.member.service;

import com.sixback.omesschat.domain.member.mapper.MemberMapper;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import com.sixback.omesschat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    public Mono<MemberInfo> findById(Long memberId) {
        return memberRepository.findById(memberId).map(MemberMapper::toMemberInfo);
    }
}

