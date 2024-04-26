package org.sixback.omess.domain.member.mapper;

import org.sixback.omess.common.utils.PasswordUtils;
import org.sixback.omess.domain.member.model.dto.request.MemberNicknameCheckResponse;
import org.sixback.omess.domain.member.model.dto.request.SignupMemberRequest;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.model.dto.response.SignInMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.SignupMemberResponse;
import org.sixback.omess.domain.member.model.entity.Member;

public class MemberMapper {

    public static Member toMember(SignupMemberRequest signupMemberRequest) {
        String encodePassword = PasswordUtils.encodePassword(signupMemberRequest.getPassword());
        return new Member(signupMemberRequest.getNickname(), signupMemberRequest.getEmail(), encodePassword);
    }

    public static Member toMember(GetMemberResponse getMemberResponse) {
        return new Member(getMemberResponse.id(), getMemberResponse.nickname(), getMemberResponse.email());
    }

    public static MemberEmailCheckResponse toMemberEmailCheckResponse(boolean isExistEmail) {
        return new MemberEmailCheckResponse(isExistEmail);
    }

    public static MemberNicknameCheckResponse toMemberNicknameCheckResponse(boolean isExist) {
        return new MemberNicknameCheckResponse(isExist);
    }

    public static GetMemberResponse toGetMemberResponse(Member member) {
        return new GetMemberResponse(member.getId(), member.getNickname(), member.getEmail());
    }

    public static SignupMemberResponse toSignupMemberResponse(Member member) {
        return new SignupMemberResponse(member.getId());
    }

    public static SignInMemberResponse toSignInMemberResponse(Member member) {
        return new SignInMemberResponse(member.getId(), member.getNickname());
    }
}
