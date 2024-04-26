package org.sixback.omess.domain.member.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInMemberRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
