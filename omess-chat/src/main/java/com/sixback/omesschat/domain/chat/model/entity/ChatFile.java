package com.sixback.omesschat.domain.chat.model.entity;

import lombok.Getter;

@Getter
public class ChatFile {
    private Long id;
    private String contentType;
    private String address;

    protected ChatFile() {
    }

    public ChatFile(Long id, String contentType, String address) {
        this.id = id;
        this.contentType = contentType;
        this.address = address;
    }
}
