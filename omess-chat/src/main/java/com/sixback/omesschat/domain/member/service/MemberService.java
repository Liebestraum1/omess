package com.sixback.omesschat.domain.member.service;

import com.sixback.omesschat.domain.file.service.FileService;
import com.sixback.omesschat.domain.member.mapper.MemberMapper;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import com.sixback.omesschat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final FileService fileService;
    private final MemberRepository memberRepository;

    public Mono<MemberInfo> findById(Long memberId) {
        return memberRepository.findById(memberId)
                .flatMap(member -> fileService
                        .findByProfile(member.getId().toString())
                        .map(fileDto -> MemberMapper.toMemberInfo(member, fileDto))
                        .switchIfEmpty(Mono.just(MemberMapper.toMemberInfo(member, null)))
                );
    }

    public Mono<MemberInfo> findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .flatMap(member -> fileService
                        .findByProfile(member.getId().toString())
                        .map(fileDto -> MemberMapper.toMemberInfo(member, fileDto))
                        .switchIfEmpty(Mono.just(MemberMapper.toMemberInfo(member, null)))
                );
    }
}

