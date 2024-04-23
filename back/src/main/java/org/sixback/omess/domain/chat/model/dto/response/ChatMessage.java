package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sixback.omess.domain.chat.model.entity.MessageType;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatMessage {
    private ResponseType type;
    private MessageType messageType;
    private String messageId;
    private ChatSubscriber writer;
    private String message;
    private LocalDateTime time;
    private Boolean isModified;
}
