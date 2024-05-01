package com.sixback.omesschat.domain.chat.model.dto.response;

import lombok.Getter;

@Getter
public class ResponseMessage {
    private ResponseType type;
    private Object data;

    private ResponseMessage() {}

    private ResponseMessage(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static ResponseMessage ok(ResponseType type, Object data) {
        return new ResponseMessage(type, data);
    }
}
