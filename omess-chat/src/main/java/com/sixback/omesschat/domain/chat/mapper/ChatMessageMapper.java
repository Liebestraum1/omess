package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.model.dto.response.ChatMessageDto;
import com.sixback.omesschat.domain.chat.model.entity.ChatMessage;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;

public class ChatMessageMapper {


    public static ChatMessageDto toResponse(ChatMessage message, MemberInfo member) {
        return new ChatMessageDto(
                member,
                message.getId(),
                message.getMessage(),
                message.getCreateAt(),
                message.isUpdated(),
                message.isPined()
        );
    }

    public static ChatMessage to(String chatId, Long memberId, String message) {
        return new ChatMessage(chatId, memberId, message);
    }
}
