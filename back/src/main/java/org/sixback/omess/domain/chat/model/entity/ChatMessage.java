package org.sixback.omess.domain.chat.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document
@Getter
public class ChatMessage {

    @MongoId
    private String id;

    private MessageType type;
    private String chatId;
    private Long memberId;
    private LocalDateTime createAt = LocalDateTime.now();

    @Setter
    private LocalDateTime updateAt;
    @Setter
    private String message;
    @Setter
    private Boolean isModified = false;
    @Setter
    private Boolean isDeleted = false;

    @Builder
    public ChatMessage(String id, MessageType type, String chatId, Long memberId, String message) {
        this.type = type;
        this.id = id;
        this.chatId = chatId;
        this.memberId = memberId;
        this.message = message;
    }
}
