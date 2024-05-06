package com.sixback.omesschat.domain.chat.service;

import com.sixback.omesschat.domain.chat.mapper.ChatMapper;
import com.sixback.omesschat.domain.chat.mapper.ChatMemberMapper;
import com.sixback.omesschat.domain.chat.model.dto.request.api.ChatCreate;
import com.sixback.omesschat.domain.chat.model.dto.response.api.ChatDto;
import com.sixback.omesschat.domain.chat.model.entity.Chat;
import com.sixback.omesschat.domain.chat.model.entity.ChatMember;
import com.sixback.omesschat.domain.chat.repository.ChatRepository;
import com.sixback.omesschat.domain.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatApiService {

    private final ChatRepository chatRepository;
    private final MemberService memberService;

    public Flux<ResponseEntity<ChatDto>> loadMyChatList(Long projectId, Long memberId) {
        return chatRepository
                .findByProjectIdAndMemberId(projectId, memberId)
                .map(ChatMapper::toChatDto)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<ChatDto>> createChat(Long projectId, Long memberId, ChatCreate create) {
        List<ChatMember> chatMembers = new ArrayList<>();

        ChatMember owner = ChatMemberMapper.to(memberId);
        chatMembers.add(owner);
        create.getEmails()
                .forEach(s ->
                        memberService
                                .findByEmail(s)
                                .map(memberInfo -> chatMembers.add(ChatMemberMapper.to(memberInfo.getId())))
                                .subscribe()
                );

        return chatRepository
                .save(ChatMapper.toChat(projectId, create.getName(), chatMembers))
                .map(ChatMapper::toChatDto)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<ChatDto>> leaveChat(Long projectId, Long memberId, String chatId) {
        //todo: 프로젝트 멤버인지 확인해야함??
        return chatRepository.findByChatIdAndMemberId(chatId, memberId)
                .map(chat -> {
                    ChatMember chatMember = chat
                            .getMembers()
                            .stream()
                            .filter(member -> memberId.equals(member.getMemberId()))
                            .findAny()
                            .orElseThrow();
                    chatMember.setAlive(false);
                    return ResponseEntity.ok(ChatMapper.toChatDto(chat));
                });
    }
}
