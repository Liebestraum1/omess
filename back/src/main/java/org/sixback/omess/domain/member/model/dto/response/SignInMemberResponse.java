package org.sixback.omess.domain.member.model.dto.response;

public record SignInMemberResponse(
        Long memberId,
        String nickname
) {
}
