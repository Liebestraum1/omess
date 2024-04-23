package org.sixback.omess.domain.apispecification.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateApiSpecificationRequest(
        @NotEmpty
        String name,

        @NotEmpty
        String category
) {
}
