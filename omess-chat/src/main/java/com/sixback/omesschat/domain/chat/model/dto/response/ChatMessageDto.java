package com.sixback.omesschat.domain.chat.model.dto.response;

import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import lombok.Getter;

@Getter
public class ChatMessageDto {
    private MemberInfo member;
    private String id;
    private String message;
    private String createAt;
    private Boolean isUpdated;

    private ChatMessageDto() {
    }

    public ChatMessageDto(MemberInfo member, String id, String message, String createAt, Boolean isUpdated) {
        this.member = member;
        this.id = id;
        this.message = message;
        this.createAt = createAt;
        this.isUpdated = isUpdated;
    }
}
