package com.sixback.omesschat.domain.chat.service;

import com.sixback.omesschat.domain.chat.mapper.ChatMapper;
import com.sixback.omesschat.domain.chat.mapper.ChatMemberMapper;
import com.sixback.omesschat.domain.chat.model.dto.request.api.ChatCreate;
import com.sixback.omesschat.domain.chat.model.dto.response.api.ChatDto;
import com.sixback.omesschat.domain.chat.model.entity.ChatMember;
import com.sixback.omesschat.domain.chat.model.entity.ChatRole;
import com.sixback.omesschat.domain.chat.repository.ChatRepository;
import com.sixback.omesschat.domain.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatApiService {

    private final ChatRepository chatRepository;
    private final MemberService memberService;

    public Flux<ChatDto> loadMyChatList(Long projectId, Long memberId) {
        return chatRepository
                .findByProjectIdAndMemberId(projectId, memberId)
                .map(ChatMapper::toChatDto);
    }

    public Mono<ResponseEntity<ChatDto>> createChat(Long projectId, Long memberId, ChatCreate create) {
        List<ChatMember> chatMembers = new ArrayList<>();

        ChatMember owner = ChatMemberMapper.to(memberId, ChatRole.OWNER);
        chatMembers.add(owner);

        return Flux.fromIterable(create.getEmails())
                .flatMap(email -> memberService.findByEmail(email)
                        .map(memberInfo -> ChatMemberMapper.to(memberInfo.getId(), ChatRole.USER)))
                .collectList()
                .flatMap(members -> {
                    chatMembers.addAll(members);
                    return chatRepository.save(ChatMapper.toChat(projectId, create.getName(), chatMembers));
                })
                .map(ChatMapper::toChatDto)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<ChatDto>> leaveChat(Long projectId, Long memberId, String chatId) {
        log.info("chatId : {}, memberId: {}", chatId, memberId);
        return chatRepository.findById(chatId)
                .map(chat -> {
                    ChatMember chatMember = chat
                            .getMembers()
                            .stream()
                            .filter(member -> memberId.equals(member.getMemberId()))
                            .findAny()
                            .orElseThrow();
                    chatMember.setAlive(false);
                    return chat;
                }).flatMap(chatRepository::save)
                .map(chat -> ResponseEntity.ok(ChatMapper.toChatDto(chat)));
    }
}
