package org.sixback.omess.domain.kanbanboard.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetKanbanBoardResponse(
        Long moduleId,
        String title,
        String category,
        List<GetIssueResponse> issues
) {
}
