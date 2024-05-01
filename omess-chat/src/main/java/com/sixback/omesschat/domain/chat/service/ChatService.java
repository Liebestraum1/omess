package com.sixback.omesschat.domain.chat.service;

import static com.sixback.omesschat.domain.chat.model.dto.response.ResponseType.HISTORY;
import static com.sixback.omesschat.domain.chat.model.dto.response.ResponseType.MESSAGE;

import com.sixback.omesschat.domain.chat.mapper.ChatMessageMapper;
import com.sixback.omesschat.domain.chat.model.dto.request.SendRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.ResponseMessage;
import com.sixback.omesschat.domain.chat.model.entity.ChatMessage;
import com.sixback.omesschat.domain.chat.repository.ChatMessageRepository;
import com.sixback.omesschat.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅 메시지를 DB에 저장하는 기능
     */
    public Flux<ResponseMessage> saveChatMessage(String chatId, Long memberId, SendRequestMessage message) {
        ChatMessage chatMessage = ChatMessageMapper.to(chatId, memberId, message.getMessage());
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
}
