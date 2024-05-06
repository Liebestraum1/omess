package com.sixback.omesschat.domain.chat.model.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class HeaderRequestMessage {
    @NotBlank
    private String detail;
}
