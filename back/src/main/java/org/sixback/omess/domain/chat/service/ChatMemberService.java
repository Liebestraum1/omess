package org.sixback.omess.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.chat.mapper.ChatMemberMapper;
import org.sixback.omess.domain.chat.model.entity.ChatMember;
import org.sixback.omess.domain.chat.model.entity.ChatRole;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.service.MemberService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final MemberService memberService;

    public List<ChatMember> createChatMember(Long memberId, List<String> emails) {
        List<ChatMember> members = new ArrayList<>();
        ChatMember owner = ChatMemberMapper.chatMemberWithMemberIdAndRole(memberId, ChatRole.OWNER);
        members.add(owner);
        emails.forEach(email ->
                members.add(
                        ChatMemberMapper.chatMemberWithMemberIdAndRole(
                                memberService.getMember(email).id(), ChatRole.USER)
                ));
        return members;
    }

    public GetMemberResponse findMemberById(Long memberId) {
        return memberService.getMember(memberId);
    }
}
