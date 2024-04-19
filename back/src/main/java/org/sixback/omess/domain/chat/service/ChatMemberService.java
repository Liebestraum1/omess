package org.sixback.omess.domain.chat.service;


import org.sixback.omess.domain.chat.model.entity.ChatMember;

public interface ChatMemberService {

    ChatMember loadChatMemberByMemberId(Long memberId);
}
