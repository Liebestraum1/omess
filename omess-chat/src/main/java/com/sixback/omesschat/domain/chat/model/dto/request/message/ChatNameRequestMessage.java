package com.sixback.omesschat.domain.chat.model.dto.request.message;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class ChatNameRequestMessage {
    @Length(min = 1, max = 50, message="채팅방 이름의 길이를 확인해 주세요.")
    private String name;
}
