package org.sixback.omess.domain.project.dto.response;

import java.util.List;

public record GetProjectMembersResponse(
        List<GetProjectMemberResponse> projects
) {
}
