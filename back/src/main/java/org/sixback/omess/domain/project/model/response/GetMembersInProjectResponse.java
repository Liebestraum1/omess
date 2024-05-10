package org.sixback.omess.domain.project.model.response;

public record GetMembersInProjectResponse(
        Long id,
        String nickname,
        String profile
) {
}
