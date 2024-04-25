package org.sixback.omess.domain.kanbanboard.model.dto.response.issue;

import lombok.Builder;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetLabelResponse;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;

@Builder
public record GetIssueResponse(
        Long issueId,
        GetMemberResponse charger,
        GetLabelResponse label,
        String title,
        Integer importance,
        Integer status
) {
}
