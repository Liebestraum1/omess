package com.sixback.omesschat.domain.chat.mapper;

import com.sixback.omesschat.domain.chat.model.dto.response.message.ChatMemberDto;
import com.sixback.omesschat.domain.chat.model.entity.ChatMember;
import com.sixback.omesschat.domain.chat.model.entity.ChatRole;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;

public class ChatMemberMapper {

    public static ChatMember to(Long memberId) {
        return new ChatMember(memberId);
    }

    public static ChatMemberDto toChatMemberDto(MemberInfo member, ChatRole role) {
        return new ChatMemberDto(member, role);
    }
}
