package org.sixback.omess.domain.chat.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Content {

    private Long memberId;
    private String content;
    private LocalDateTime createAt = LocalDateTime.now();

    @Builder
    public Content(Long memberId, String content) {
        this.memberId = memberId;
        this.content = content;
    }
}
