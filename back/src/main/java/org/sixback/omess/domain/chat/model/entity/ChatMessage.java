package org.sixback.omess.domain.chat.model.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Long memberId;
    private LocalDateTime createAt = LocalDateTime.now();

    @Setter
    private String message;
    @Setter
    private LocalDateTime updateAt;
    @Setter
    private boolean isDeleted = false;
    @Setter
    private boolean isUpdated = false;

    public ChatMessage(String chatId, Long memberId, String message) {
        this.chatId = chatId;
        this.memberId = memberId;
        this.message = message;
    }
}
