package org.sixback.omess.domain.apispecification.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateApiSpecificationRequest(
        @NotEmpty
        @Size(min = 1, max = 90)
        String name,

        @NotEmpty
        @Size(min = 1, max = 20)
        String category
) {
}
