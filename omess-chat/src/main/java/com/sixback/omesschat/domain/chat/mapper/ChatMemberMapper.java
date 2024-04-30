package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.model.entity.ChatMember;

public class ChatMemberMapper {

    public static ChatMember to(Long memberId) {
        return new ChatMember(memberId);
    }
}
