package com.sixback.omesschat.domain.chat.model.dto.response;

import lombok.Getter;

@Getter
public class ChatHeaderMessage {
    private String detail;
    private ChatMessageDto message;

    public ChatHeaderMessage() {
    }

    public ChatHeaderMessage(String detail, ChatMessageDto message) {
        this.detail = detail;
        this.message = message;
    }
}
