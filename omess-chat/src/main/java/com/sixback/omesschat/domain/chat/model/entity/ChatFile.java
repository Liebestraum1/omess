package com.sixback.omesschat.domain.chat.model.entity;

import lombok.Getter;

@Getter
public class ChatFile {
    private Long id;
    private String address;

    protected ChatFile() {
    }

    public ChatFile(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
