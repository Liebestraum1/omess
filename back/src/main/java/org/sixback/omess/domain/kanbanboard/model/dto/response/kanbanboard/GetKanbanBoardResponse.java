package org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard;

import lombok.Builder;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueResponse;

import java.util.List;

@Builder
public record GetKanbanBoardResponse(
        Long moduleId,
        String title,
        String category,
        List<GetIssueResponse> issues
) {
}
