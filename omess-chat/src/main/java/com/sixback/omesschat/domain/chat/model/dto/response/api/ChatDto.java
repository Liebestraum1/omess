package com.sixback.omesschat.domain.chat.model.dto.response.api;

import lombok.Getter;

@Getter
public class ChatDto {
    private String id;
    private String name;
    private String header;
    private int memberCount;
    private Long pinCount;

    protected ChatDto() {
    }

    public ChatDto(String id, String name, String header, int memberCount, Long pinCount) {
        this.id = id;
        this.name = name;
        this.header = header;
        this.memberCount = memberCount;
        this.pinCount = pinCount;
    }
}
