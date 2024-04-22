package org.sixback.omess.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sixback.omess.domain.member.mapper.MemberMapper.toMemberEmailCheckResponse;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberEmailCheckResponse isExistEmail(String email) {
        return toMemberEmailCheckResponse(memberRepository.existsMemberByEmail(email));
    }
}
