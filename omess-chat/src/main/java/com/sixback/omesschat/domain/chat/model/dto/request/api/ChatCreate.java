package com.sixback.omesschat.domain.chat.model.dto.request.api;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatCreate {
    private List<String> emails;
    private String name;
}
