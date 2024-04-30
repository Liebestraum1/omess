package com.sixback.omesschat.domain.chat.parser;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixback.omesschat.domain.chat.model.message.Message;

import java.util.Map;

public class MessageParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseMessage(Map<String, Object> data, Class<T> target) {
        return mapper.convertValue(data, target);
    }

    public static Message parseMessage(String data) {
        try {
            return mapper.readValue(data, Message.class);
        } catch (JacksonException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static String valueToString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
