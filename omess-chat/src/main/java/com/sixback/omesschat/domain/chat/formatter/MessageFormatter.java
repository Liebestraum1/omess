package com.sixback.omesschat.domain.chat.formatter;

import com.sixback.omesschat.domain.chat.model.entity.MessageType;

public class MessageFormatter {

    public static String ofType(MessageType type, String message, String writer) {
        if (type == MessageType.SYSTEM) {
            return writer + " 님이 " + message + " 로 변경";
        }
        return message;
    }
}
