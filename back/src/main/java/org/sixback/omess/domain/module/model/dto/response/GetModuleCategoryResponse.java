package org.sixback.omess.domain.module.model.dto.response;

import lombok.Builder;

@Builder
public record GetModuleCategoryResponse(
        Long id,
        String category,
        String path
) {
}
