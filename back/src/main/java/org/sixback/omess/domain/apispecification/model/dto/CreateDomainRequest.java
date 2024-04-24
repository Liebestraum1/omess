package org.sixback.omess.domain.apispecification.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateDomainRequest(
        @NotEmpty
        @Size(max= 20)
        String name
) {
}
