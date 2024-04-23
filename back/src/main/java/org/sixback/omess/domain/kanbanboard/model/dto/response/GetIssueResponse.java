package org.sixback.omess.domain.kanbanboard.model.dto.response;

import lombok.Builder;

@Builder
public record GetIssueResponse(
        Long issueId,
        //FixMe 담당자 정보 추가
        GetLabelResponse label,
        String title,
        int importance,
        int status
) {
}
