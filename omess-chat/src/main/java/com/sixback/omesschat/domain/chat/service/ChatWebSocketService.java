package com.sixback.omesschat.domain.chat.service;

import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.CHAT_NAME;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.DELETE;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.HEADER;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.HISTORY;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.LOAD_PIN;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.MEMBERS;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.MESSAGE;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.PIN;
import static com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseType.UPDATE;

import com.sixback.omesschat.domain.chat.mapper.ChatMapper;
import com.sixback.omesschat.domain.chat.mapper.ChatMemberMapper;
import com.sixback.omesschat.domain.chat.mapper.ChatMessageMapper;
import com.sixback.omesschat.domain.chat.model.dto.request.message.SendRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseMessage;
import com.sixback.omesschat.domain.chat.model.entity.ChatMember;
import com.sixback.omesschat.domain.chat.model.entity.ChatMessage;
import com.sixback.omesschat.domain.chat.model.entity.Content;
import com.sixback.omesschat.domain.chat.repository.ChatMessageRepository;
import com.sixback.omesschat.domain.chat.repository.ChatRepository;
import com.sixback.omesschat.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatWebSocketService {

    private static final int PAGE_SIZE = 20;
    private final MemberService memberService;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅 메시지를 DB에 저장하는 기능
     */
    public Flux<ResponseMessage> saveChatMessage(String chatId, Long memberId, SendRequestMessage message) {
        ChatMessage chatMessage = ChatMessageMapper.toUserMessage(chatId, memberId, message.getMessage(),
                message.getFiles());
        return chatMessageRepository.save(chatMessage)
                .flatMap(c -> memberService
                        .findById(memberId)
                        .map(member -> ChatMessageMapper.toResponse(c, member))
                )
                .doOnSuccess(chatMessageDto -> log.info("저장 성공 !!!"))
                .doOnError(throwable -> log.error("에러 발생 - {} : {}", throwable.getCause(), throwable.getMessage()))
                .map(chatMessageDto -> ResponseMessage.ok(MESSAGE, chatMessageDto))
                .flux();
    }

    /**
     * 채팅 내역을 불러오는 기능
     */
    public Flux<ResponseMessage> loadChatHistory(String chatId, int offset) {
        log.info("채팅 내역 불러오기...");
        return chatMessageRepository.findAllByChatId(chatId)
                .sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()))
                .skip(offset)
                .take(PAGE_SIZE)
                .flatMap(chatMessage -> memberService
                        .findById(chatMessage.getWriter())
                        .map(memberInfo -> ChatMessageMapper.toResponse(chatMessage, memberInfo))
                ).map(chatMessageDto -> ResponseMessage.ok(HISTORY, chatMessageDto))
                .doOnComplete(() -> log.info("채팅 내역 불러오기 성공 !!!"));
    }

    /**
     * 채팅 메시지를 업데이트 하는 기능
     */
    public Flux<ResponseMessage> updateChatMessage(Long memberId, String messageId, String message) {
        log.info("채팅 메시지 업데이트");
        return chatMessageRepository.findById(messageId)
                .doOnNext(m -> {
                    if (!memberId.equals(m.getWriter())) {
                        throw new IllegalArgumentException();
                    }
                }).map(m -> m.update(message)).flatMap(chatMessageRepository::save)
                .flatMap(m -> memberService
                        .findById(memberId)
                        .map(memberInfo -> ChatMessageMapper.toResponse(m, memberInfo))
                ).map(m -> ResponseMessage.ok(UPDATE, m))
                .flux();
    }

    /**
     * 채팅 메시지 삭제 기능
     */
    public Flux<ResponseMessage> deleteChatMessage(Long memberId, String messageId) {
        log.info("채팅 메시지 삭제");
        return chatMessageRepository.findById(messageId)
                .doOnNext(m -> {
                    if (!memberId.equals(m.getWriter())) {
                        throw new IllegalArgumentException();
                    }
                }).map(ChatMessage::delete)
                .flatMap(chatMessageRepository::save)
                .flatMap(m -> memberService
                        .findById(memberId)
                        .map(memberInfo -> ChatMessageMapper.toResponse(m, memberInfo))
                ).map(m -> ResponseMessage.ok(DELETE, m))
                .flux();
    }

    /**
     * 채팅 핀 기능
     */
    public Flux<ResponseMessage> pinChatMessage(Long memberId, String messageId) {
        log.info("채팅 핀 기능");
        return chatMessageRepository.findById(messageId)
                .flatMap(chatMessage -> {
                    chatMessage.pin();
                    return chatMessageRepository.save(chatMessage)
                            .flatMap(savedChatMessage -> chatRepository.findById(savedChatMessage.getChatId())
                                        .flatMap(chat -> {
                                            if (savedChatMessage.isPinned()) {
                                                chat.pinned();
                                            } else {
                                                chat.unpinned();
                                            }
                                            return chatRepository.save(chat);
                                        })
                                        .thenReturn(savedChatMessage));
                })
                .flatMap(chatMessage -> memberService.findById(memberId)
                        .map(memberInfo -> ChatMessageMapper.toResponse(chatMessage, memberInfo)))
                .map(response -> ResponseMessage.ok(PIN, response))
                .doOnNext(responseMessage -> log.info("{}", responseMessage.getData()))
                .flux();
    }

    /**
     * 헤더 등록
     */
    public Flux<ResponseMessage> registerHeader(String chatId, Long memberId, String detail) {
        log.info("헤더 등록");
        Content header = ChatMapper.toContent(memberId, detail);
        return chatRepository.findById(chatId)
                .map(chat -> chat.update(header))
                .flatMap(chatRepository::save)
                .map(chat -> ChatMessageMapper.toSystemMessage(chatId, memberId, header.getDetail()))
                .flatMap(chatMessageRepository::save)
                .flatMap(m -> memberService
                        .findById(memberId)
                        .map(memberInfo -> ChatMessageMapper.toResponse(m, memberInfo))
                )
                .map(chatMessageDto -> ChatMapper.toHeaderResponse(chatMessageDto, header))
                .map(m -> ResponseMessage.ok(HEADER, m))
                .flux();
    }

    /**
     * 채팅방 이름 변경
     */
    public Flux<ResponseMessage> modifyChatName(String chatId, Long memberId, String name) {
        log.info("채팅방 이름 변경");
        return chatRepository.findById(chatId)
                .map(chat -> chat.update(name))
                .flatMap(chatRepository::save)
                .map(chat -> ChatMessageMapper.toSystemMessage(chatId, memberId, name))
                .flatMap(chatMessageRepository::save)
                .flatMap(m -> memberService
                        .findById(memberId)
                        .map(memberInfo -> ChatMessageMapper.toResponse(m, memberInfo))
                )
                .map(chatMessageDto -> ChatMapper.toChatNameResponse(name, chatMessageDto))
                .map(m -> ResponseMessage.ok(CHAT_NAME, m))
                .flux();
    }

    /**
     * 채팅방 멤버 불러오기
     */
    public Flux<ResponseMessage> loadChatMembers(String chatId) {
        return chatRepository
                .findById(chatId)
                .flatMapMany(chat -> Flux.fromIterable(chat.getMembers()))
                .filter(ChatMember::isAlive)
                .flatMap(chatMember -> {
                    Long memberId = chatMember.getMemberId();
                    return memberService
                            .findById(memberId)
                            .map(memberInfo ->
                                    ChatMemberMapper.toChatMemberDto(memberInfo, chatMember.getRole())
                            );
                })
                .map(chatMemberDto -> ResponseMessage.ok(MEMBERS, chatMemberDto));
    }

    /**
     * 채팅방 핀 불러오기
     */
    public Flux<ResponseMessage> loadPinMessages(String chatId, int offset) {
        return chatMessageRepository.findPinMessageByChatId(chatId)
                .sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()))
                .skip(offset)
                .take(PAGE_SIZE)
                .flatMap(chatMessage -> memberService
                        .findById(chatMessage.getWriter())
                        .map(memberInfo -> ChatMessageMapper.toResponse(chatMessage, memberInfo))
                ).map(chatMessageDto -> ResponseMessage.ok(LOAD_PIN, chatMessageDto))
                .doOnComplete(() -> log.info("핀 내역 불러오기 성공 !!!"));
    }

}
