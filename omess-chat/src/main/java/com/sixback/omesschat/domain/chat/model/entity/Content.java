package com.sixback.omesschat.domain.chat.model.entity;

import lombok.Getter;

@Getter
public class Content {

    private String detail = "";
    private Long writer;

    protected Content() {
    }

    public static Content empty() {
        return new Content();
    }

    public Content(String detail, Long writer) {
        this.detail = detail;
        this.writer = writer;
    }
}
