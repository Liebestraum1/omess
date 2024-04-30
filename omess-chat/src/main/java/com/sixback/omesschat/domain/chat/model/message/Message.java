package com.sixback.omesschat.domain.chat.model.message;

import lombok.Getter;

import java.util.Map;

@Getter
public class Message {
    private MessageType type;
    private Map<String, Object> data;

}
