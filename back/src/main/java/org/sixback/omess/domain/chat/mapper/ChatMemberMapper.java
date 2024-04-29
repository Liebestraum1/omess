package org.sixback.omess.domain.chat.mapper;

import org.sixback.omess.domain.chat.model.dto.response.ChatSubscriber;
import org.sixback.omess.domain.chat.model.entity.ChatMember;
import org.sixback.omess.domain.chat.model.entity.ChatRole;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;

public class ChatMemberMapper {

    public static ChatMember chatMemberWithMemberIdAndRole(Long memberId, ChatRole role) {
        return ChatMember.builder()
                .memberId(memberId)
                .role(role)
                .build();
    }

    public static ChatSubscriber memberToChatSubscriber(GetMemberResponse member) {
        return ChatSubscriber.builder()
                .memberId(member.id())
                .email(member.email())
                .nickname(member.nickname())
                .build();
    }
}
