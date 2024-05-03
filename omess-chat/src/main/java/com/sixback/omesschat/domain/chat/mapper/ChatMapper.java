package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.model.dto.response.ChatHeaderMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.ChatMessageDto;
import com.sixback.omesschat.domain.chat.model.entity.Content;

public class ChatMapper {

    public static Content toContent(Long memberId, String detail) {
        return new Content(detail, memberId);
    }

    public static ChatHeaderMessage toHeaderResponse(ChatMessageDto messageDto, Content content) {
        return new ChatHeaderMessage(content.getDetail(), messageDto);
    }
}
