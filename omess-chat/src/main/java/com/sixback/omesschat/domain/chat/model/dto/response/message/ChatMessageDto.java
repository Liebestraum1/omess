package com.sixback.omesschat.domain.chat.model.dto.response.message;

import com.sixback.omesschat.domain.chat.model.entity.ChatFile;
import com.sixback.omesschat.domain.chat.model.entity.MessageType;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatMessageDto {
    private MessageType classify;
    private MemberInfo member;
    private String id;
    private String message;
    private String createAt;
    private Boolean isUpdated;
    private Boolean isPined;
    private List<ChatFile> files;

    private ChatMessageDto() {
    }

    public ChatMessageDto(
            MessageType classify,
            MemberInfo member,
            String id,
            String message,
            String createAt,
            Boolean isUpdated,
            Boolean isPined,
            List<ChatFile> files
    ) {
        this.classify = classify;
        this.member = member;
        this.id = id;
        this.message = message;
        this.createAt = createAt;
        this.isUpdated = isUpdated;
        this.isPined = isPined;
        this.files = files;
    }
}
