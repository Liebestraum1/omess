package org.sixback.omess.domain.kanbanboard.model.dto.response.label;

import lombok.Builder;

import java.util.List;

@Builder
public record GetLabelResponses(List<GetLabelResponse> labels) {
}
