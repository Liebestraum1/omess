package org.sixback.omess.domain.member.model.dto.response;

import lombok.Builder;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueResponse;

import java.util.List;

@Builder
public record GetIssueResponses(List<GetIssueResponse> issues) {
}
