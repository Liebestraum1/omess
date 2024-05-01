package org.sixback.omess.domain.kanbanboard.model.dto.response.issue;

import lombok.Builder;
import org.sixback.omess.domain.kanbanboard.model.dto.response.label.GetLabelResponse;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;

@Builder
public record GetIssueDetailResponse(
        Long issueId,
        GetMemberResponse charger,
        GetLabelResponse label,
        String title,
        String content,
        Integer importance,
        Integer status
) {
}
