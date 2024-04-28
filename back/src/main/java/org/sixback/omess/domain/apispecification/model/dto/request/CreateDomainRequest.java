package org.sixback.omess.domain.apispecification.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateDomainRequest(
        @NotEmpty
        @Size(min = 1, max= 20)
        String name
) {
}
