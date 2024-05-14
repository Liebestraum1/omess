package com.sixback.omesschat.domain.chat.model.entity;

import lombok.Getter;

@Getter
public class ChatFile {
    private Long id;
    private String contentType;
    private String originalName;
    private String address;

    protected ChatFile() {
    }

    public ChatFile(Long id, String originalName, String contentType, String address) {
        this.id = id;
        this.originalName = originalName;
        this.contentType = contentType;
        this.address = address;
    }
}
