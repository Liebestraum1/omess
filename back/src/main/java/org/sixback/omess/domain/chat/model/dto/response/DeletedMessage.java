package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeletedMessage {
    private ResponseType type = ResponseType.DELETED;
    private String messageId;

    @Builder
    public DeletedMessage(String messageId) {
        this.messageId = messageId;
    }
}
