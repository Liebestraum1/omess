package com.sixback.omesschat.domain.chat.model.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteRequestMessage {
    @NotBlank(message = "메시지 id 입력")
    private String messageId;
}
