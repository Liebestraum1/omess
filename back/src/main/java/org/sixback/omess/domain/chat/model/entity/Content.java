package org.sixback.omess.domain.chat.model.entity;

import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
public class Content {

    private String contentId = UUID.randomUUID().toString();
    private Long memberId;
    private String subject;
    private LocalDateTime time = LocalDateTime.now();
    private Boolean isModified = false;
    private Boolean isDeleted = false;

    @Builder
    public Content(Long memberId, String subject) {
        this.memberId = memberId;
        this.subject = subject;
    }
}
