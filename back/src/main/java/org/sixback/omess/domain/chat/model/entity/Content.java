package org.sixback.omess.domain.chat.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class Content {

    private MessageType type;
    private String contentId = UUID.randomUUID().toString();
    private Long memberId;
    private String subject;
    private LocalDateTime time = LocalDateTime.now();
    private Boolean isModified = false;
    private Boolean isDeleted = false;

    @Builder
    public Content(MessageType type, Long memberId, String subject) {
        this.type = type;
        this.memberId = memberId;
        this.subject = subject;
    }
}
