package org.sixback.omess.domain.module.model.dto.response;

import lombok.Builder;

@Builder
public record GetModuleResponse(
        Long id,
        String title,
        String categoty
) {
}
