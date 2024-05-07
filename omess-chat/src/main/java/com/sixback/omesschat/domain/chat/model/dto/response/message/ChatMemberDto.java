package com.sixback.omesschat.domain.chat.model.dto.response.message;

import com.sixback.omesschat.domain.chat.model.entity.ChatRole;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import lombok.Getter;

@Getter
public class ChatMemberDto {
    MemberInfo member;
    ChatRole role;

    protected ChatMemberDto() {

    }

    public ChatMemberDto(MemberInfo member, ChatRole role) {
        this.member = member;
        this.role = role;
    }
}
