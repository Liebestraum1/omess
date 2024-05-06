package com.sixback.omesschat.domain.chat.model.dto.response.api;

public class ChatDto {
    private String id;
    private String name;
    private String header;
    private int memberCount;

    protected ChatDto() {
    }

    public ChatDto(String id, String name, String header, int memberCount) {
        this.id = id;
        this.name = name;
        this.header = header;
        this.memberCount = memberCount;
    }
}
