package com.sixback.omesschat.domain.chat.model.dto.request.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatCreate {
    @Size(min = 1)
    private List<String> emails;
    @NotBlank
    private String name;

    protected ChatCreate() {}

    public ChatCreate(List<String> emails, String name) {
        this.emails = emails;
        this.name = name;
    }
}
