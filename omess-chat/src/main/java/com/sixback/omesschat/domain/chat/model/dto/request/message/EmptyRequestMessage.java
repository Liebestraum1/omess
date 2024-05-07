package com.sixback.omesschat.domain.chat.model.dto.request.message;

import lombok.Getter;

@Getter
public class EmptyRequestMessage {
    private RequestType type;

    public EmptyRequestMessage(RequestType type) {
        this.type = type;
    }
}
