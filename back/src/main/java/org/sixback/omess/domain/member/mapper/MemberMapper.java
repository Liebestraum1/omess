package org.sixback.omess.domain.member.mapper;

import org.sixback.omess.domain.member.model.dto.request.MemberNicknameCheckResponse;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.model.entity.Member;

public class MemberMapper {
    public static MemberEmailCheckResponse toMemberEmailCheckResponse(boolean isExistEmail) {
        return new MemberEmailCheckResponse(isExistEmail);
    }

    public static GetMemberResponse toGetMemberResponse(Member member) {
        return new GetMemberResponse(member.getId(), member.getNickname(), member.getEmail());
    }

    public static MemberNicknameCheckResponse toMemberNicknameCheckResponse(boolean isExist) {
        return new MemberNicknameCheckResponse(isExist);
    }

    public static Member toMember(GetMemberResponse getMemberResponse) {
        return new Member(getMemberResponse.id(), getMemberResponse.nickname(), getMemberResponse.email());
    }
}
