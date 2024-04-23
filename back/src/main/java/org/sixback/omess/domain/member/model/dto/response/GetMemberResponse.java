package org.sixback.omess.domain.member.model.dto.response;

public record GetMemberResponse(
        Long id,
        String nickname,
        String email
) {
}
