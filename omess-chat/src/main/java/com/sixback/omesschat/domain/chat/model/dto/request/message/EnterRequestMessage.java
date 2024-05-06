package com.sixback.omesschat.domain.chat.model.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EnterRequestMessage {
    @NotBlank
    private String chatId;
    @NotNull
    private Long memberId;
}
