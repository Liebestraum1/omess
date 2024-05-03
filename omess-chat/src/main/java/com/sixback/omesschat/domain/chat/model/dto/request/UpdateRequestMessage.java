package com.sixback.omesschat.domain.chat.model.dto.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UpdateRequestMessage {
    private String messageId;
    @Length(min = 1, max = 16383, message = "메시지 길이를 확인해 주세요.")
    private String message;
}
