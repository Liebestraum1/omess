package org.sixback.omess.domain.chat.mapper;

import org.sixback.omess.domain.chat.model.dto.response.ChatMessageDto;
import org.sixback.omess.domain.chat.model.dto.response.ResponseType;
import org.sixback.omess.domain.chat.model.entity.ChatMessage;
import org.sixback.omess.domain.chat.model.entity.MessageType;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;

public class ChatMessageMapper {

    public static ChatMessageDto toResponse(ChatMessage message, GetMemberResponse member, ResponseType responseType) {
        boolean isModified = message.getIsModified();
        return ChatMessageDto.builder()
                .messageId(message.getId())
                .message(message.getMessage())
                .type(responseType)
                .messageType(message.getType())
                .isModified(isModified)
                .time(isModified ? message.getUpdateAt() : message.getCreateAt())
                .writer(ChatMemberMapper.memberToChatSubscriber(member))
                .build();
    }

    public static ChatMessage entityFrom(String roomId, Long memberId, String message, MessageType type) {
        return ChatMessage.builder()
                .chatId(roomId)
                .memberId(memberId)
                .message(message)
                .type(type)
                .build();
    }
}
