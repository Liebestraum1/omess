package com.sixback.omesschat.domain.chat.model.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @MongoId
    private String id;

    private String chatId;
    private Long writer;
    private LocalDateTime createAt = LocalDateTime.now();

    private String message;
    private LocalDateTime updateAt;
    private boolean isUpdated = false;
    private boolean isDeleted = false;

    public ChatMessage(String chatId, Long writer, String message) {
        this.chatId = chatId;
        this.writer = writer;
        this.message = message;
    }

    public ChatMessage update(String message) {
        this.message = message;
        this.isUpdated = true;
        this.updateAt = LocalDateTime.now();
        return this;
    }

    public ChatMessage delete() {
        this.isDeleted = true;
        return this;
    }
}
