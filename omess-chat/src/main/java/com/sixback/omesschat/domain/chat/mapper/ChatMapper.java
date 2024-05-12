package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.model.dto.response.api.ChatDto;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ChatHeaderMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ChatMessageDto;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ChatNameMessage;
import com.sixback.omesschat.domain.chat.model.entity.Chat;
import com.sixback.omesschat.domain.chat.model.entity.ChatMember;
import com.sixback.omesschat.domain.chat.model.entity.Content;
import java.util.List;

public class ChatMapper {

    public static Content toContent(Long memberId, String detail) {
        return new Content(detail, memberId);
    }

    public static ChatHeaderMessage toHeaderResponse(ChatMessageDto messageDto, Content content) {
        return new ChatHeaderMessage(content.getDetail(), messageDto);
    }

    public static ChatNameMessage toChatNameResponse(String chatName, ChatMessageDto chatMessageDto) {
        return new ChatNameMessage(chatName, chatMessageDto);
    }

    public static ChatDto toChatDto(Chat chat) {
        return new ChatDto(chat.getId(), chat.getName(), chat.getHeader().getDetail(), chat.getMembers().size(), chat.getPinCount());
    }

    public static Chat toChat(Long projectId, String name, List<ChatMember> chatMembers) {
        return new Chat(projectId, name, chatMembers);
    }
}
