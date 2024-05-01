package com.sixback.omesschat.domain.chat.model.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class RequestMessage {
    private RequestType type;
    private Map<String, Object> data;

}
