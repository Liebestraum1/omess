package com.sixback.omesschat.domain.chat.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PinRequestMessage {
    @NotBlank
    private String messageId;
}
