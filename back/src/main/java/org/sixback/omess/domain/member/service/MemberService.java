package org.sixback.omess.domain.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.exception.MemberErrorMessage;
import org.sixback.omess.domain.member.model.dto.request.MemberNicknameCheckResponse;
import org.sixback.omess.domain.member.model.dto.request.SignInMemberRequest;
import org.sixback.omess.domain.member.model.dto.request.SignupMemberRequest;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.model.dto.response.SignInMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.SignupMemberResponse;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sixback.omess.common.utils.PasswordUtils.isNotValidPassword;
import static org.sixback.omess.domain.member.exception.MemberErrorMessage.MEMBER_NOT_FOUND;
import static org.sixback.omess.domain.member.mapper.MemberMapper.*;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public GetMemberResponse getMember(Long memberId) {
        getEntity(memberId);
        return toGetMemberResponse(getEntity(memberId));
    }

    @Transactional(readOnly = true)
    public GetMemberResponse getMember(String email) {
        return toGetMemberResponse(getEntity(email));
    }

    @Transactional(readOnly = true)
    public MemberEmailCheckResponse isExistEmail(String email) {
        return toMemberEmailCheckResponse(memberRepository.existsMemberByEmail(email));
    }

    @Transactional(readOnly = true)
    public MemberNicknameCheckResponse isExistNickname(String nickname) {
        return toMemberNicknameCheckResponse(memberRepository.existsMemberByNickname(nickname));
    }

    @Transactional
    public SignupMemberResponse signup(SignupMemberRequest signupMemberRequest) {
        return toSignupMemberResponse(memberRepository.save(toMember(signupMemberRequest)));
    }

    @Transactional
    public SignInMemberResponse signin(SignInMemberRequest signInMemberRequest) {
        Member member = checkValidSignin(signInMemberRequest);
        return toSignInMemberResponse(member);
    }

    @Transactional(readOnly = true)
    protected Member getEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    protected Member getEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND.getMessage()));
    }

    protected Member checkValidSignin(SignInMemberRequest signInMemberRequest) {
        Member foundMember;
        try {
            foundMember = getEntity(signInMemberRequest.email());
        } catch (EntityNotFoundException exception) {
            throw new InsufficientAuthenticationException(exception.getMessage());
        }

        if (isNotValidPassword(signInMemberRequest.password(), foundMember.getPassword())) {
            throw new InsufficientAuthenticationException(MemberErrorMessage.MEMBER_AUTHENTICATION_FAIL.getMessage());
        }
        return foundMember;
    }
}
