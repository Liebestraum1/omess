package com.sixback.omesschat.domain.chat.model.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @MongoId
    private String id;

    private MessageType classify;

    private String chatId;
    private Long writer;
    private List<ChatFile> files;
    private String createAt = time();

    private String message;
    private String updateAt;
    private boolean isUpdated = false;
    private boolean isDeleted = false;
    private boolean isPinned = false;

    public ChatMessage(MessageType classify, String chatId, Long writer, String message, List<ChatFile> files) {
        this.classify = classify;
        this.chatId = chatId;
        this.writer = writer;
        this.message = message;
        this.files = files;
    }

    public ChatMessage update(String message) {
        this.message = message;
        this.isUpdated = true;
        this.updateAt = time();
        return this;
    }

    public ChatMessage delete() {
        this.isDeleted = true;
        return this;
    }

    public ChatMessage pin() {
        isPinned = !isPinned;
        return this;
    }

    public String time() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return now.format(formatter);
    }
}
