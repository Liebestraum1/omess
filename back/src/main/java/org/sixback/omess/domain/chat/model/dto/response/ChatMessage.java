package org.sixback.omess.domain.chat.model.dto.response;

import lombok.*;
import org.sixback.omess.domain.chat.model.entity.MessageType;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    private ResponseType type;
    private MessageType messageType;
    private String messageId;
    private ChatSubscriber writer;
    private String message;
    private LocalDateTime time;
    private Boolean isModified;
}
