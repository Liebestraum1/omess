package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatInfo {
    private ResponseType type = ResponseType.CHAT_INFO;
    private String id;
    private String name;
    private int count;

    @Builder
    public ChatInfo(String id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
