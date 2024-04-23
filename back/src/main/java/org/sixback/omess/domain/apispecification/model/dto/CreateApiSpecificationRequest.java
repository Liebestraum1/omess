package org.sixback.omess.domain.apispecification.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateApiSpecificationRequest(
        @NotEmpty
        String name,

        @NotEmpty
        String category,

        @NotNull
        Long projectId
) {
}
