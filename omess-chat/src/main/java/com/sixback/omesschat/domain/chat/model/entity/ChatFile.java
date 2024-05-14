package com.sixback.omesschat.domain.chat.model.entity;

import lombok.Getter;

@Getter
public class ChatFile {
    private Long id;
    private String contentType;
    private String filename;
    private String address;

    protected ChatFile() {
    }

    public ChatFile(Long id, String filename, String contentType, String address) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.address = address;
    }
}
