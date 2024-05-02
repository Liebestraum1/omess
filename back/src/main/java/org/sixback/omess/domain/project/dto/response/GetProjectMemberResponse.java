package org.sixback.omess.domain.project.dto.response;

public record GetProjectMemberResponse(
        Long projectId,
        String name
) {
}
