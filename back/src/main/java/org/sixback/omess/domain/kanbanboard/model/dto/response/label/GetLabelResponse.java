package org.sixback.omess.domain.kanbanboard.model.dto.response.label;

import lombok.Builder;

@Builder
public record GetLabelResponse(
        Long labelId,
        String name
) {
}
