package com.sixback.omesschat.domain.chat.model.dto.response.message;

import lombok.Getter;

@Getter
public class ChatNameMessage {
    private String chatName;
    private ChatMessageDto message;

    public ChatNameMessage() {
    }

    public ChatNameMessage(String chatName, ChatMessageDto message) {
        this.chatName = chatName;
        this.message = message;
    }
}
