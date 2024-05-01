package com.sixback.omesschat.domain.chat.model.dto.request;

import lombok.Getter;

@Getter
public class EnterRequestMessage {
    private String chatId;
    private Long memberId;
}
