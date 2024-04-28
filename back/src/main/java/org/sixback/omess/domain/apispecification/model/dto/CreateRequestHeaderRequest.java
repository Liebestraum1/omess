package org.sixback.omess.domain.apispecification.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateRequestHeaderRequest(
        @NotEmpty
        @Size(min = 1, max = 50)
        String headerKey,

        @NotEmpty
        @Size(min = 1, max = 100)
        String headerValue
) {

}
