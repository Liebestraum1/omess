package org.sixback.omess.domain.member.mapper;

import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;

public class MemberMapper {
    public static MemberEmailCheckResponse toMemberEmailCheckResponse(boolean isExistEmail) {
        return new MemberEmailCheckResponse(isExistEmail);
    }
}
