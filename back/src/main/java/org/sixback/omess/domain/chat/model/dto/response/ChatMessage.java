package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatMessage {
    private ChatSubscriber writer;
    private String message;
    private LocalDateTime time;
    private Boolean isModified;
}
