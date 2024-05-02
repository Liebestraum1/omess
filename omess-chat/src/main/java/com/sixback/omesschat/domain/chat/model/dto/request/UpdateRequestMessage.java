package com.sixback.omesschat.domain.chat.model.dto.request;

import lombok.Getter;

@Getter
public class UpdateRequestMessage {
    private String messageId;
    private String message;
}
