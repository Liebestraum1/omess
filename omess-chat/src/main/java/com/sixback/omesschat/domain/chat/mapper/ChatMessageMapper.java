package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.formatter.MessageFormatter;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ChatMessageDto;
import com.sixback.omesschat.domain.chat.model.entity.ChatFile;
import com.sixback.omesschat.domain.chat.model.entity.ChatMessage;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;

import java.util.List;

import static com.sixback.omesschat.domain.chat.model.entity.MessageType.SYSTEM;
import static com.sixback.omesschat.domain.chat.model.entity.MessageType.USER;

public class ChatMessageMapper {

    public static ChatMessageDto toResponse(ChatMessage message, MemberInfo member) {
        return new ChatMessageDto(
                message.getClassify(),
                member,
                message.getId(),
                MessageFormatter.ofType(message.getClassify(), message.getMessage(), member.getNickname()),
                message.getCreateAt(),
                message.isUpdated(),
                message.isPined(),
                message.getFiles()
        );
    }

    public static ChatMessage toUserMessage(String chatId, Long memberId, String message, List<ChatFile> files) {
        return new ChatMessage(USER, chatId, memberId, message, files);
    }

    public static ChatMessage toSystemMessage(String chatId, Long memberId, String message) {
        return new ChatMessage(SYSTEM, chatId, memberId, message, List.of());
    }

}
