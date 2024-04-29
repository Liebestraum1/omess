package org.sixback.omess.domain.chat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.config.websocket.StompUser;
import org.sixback.omess.domain.chat.model.dto.payload.MessagePayload;
import org.sixback.omess.domain.chat.model.dto.payload.RolePayload;
import org.sixback.omess.domain.chat.model.dto.response.*;
import org.sixback.omess.domain.chat.model.entity.MessageType;
import org.sixback.omess.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/load/{roomId}")
    @SendToUser("/sub/{roomId}")
    public Flux<ChatMessageDto> getChatHistory(@DestinationVariable String roomId, @Payload int offset) {
        return chatService.loadChatMessageList(roomId, offset);
    }

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public Mono<ChatMessageDto> sendMessage(
            @DestinationVariable String roomId,
            @Payload MessagePayload message,
            Principal user
    ) {
        log.info("in sendMessage() : {}", message.getSubject());
        StompUser stompUser = (StompUser) user;
        return chatService.saveChatMessage(
                roomId,
                stompUser.getMemberId(),
                message.getSubject(),
                message.getMessageType());
    }

//    @MessageMapping("/{roomId}/header")
//    @SendTo("/sub/{roomId}")
//    public Mono<ChatMessageDto> changeHeader(
//            @DestinationVariable String roomId,
//            @Payload String subject,
//            Principal user
//    ) {
//        StompUser stompUser = (StompUser) user;
//        return chatService.update(roomId, stompUser.getMemberId(), subject, MessageType.HEADER);
//    }

//    @MessageMapping("/{roomId}/notice")
//    @SendTo("/sub/{roomId}")
//    public Mono<ChatMessageDto> changeNotice(
//            @DestinationVariable String roomId,
//            @Payload String subject,
//            Principal user
//    ) {
//        StompUser stompUser = (StompUser) user;
//        return chatService.update(roomId, stompUser.getMemberId(), subject, MessageType.NOTICE);
//    }

    @MessageMapping("/{roomId}/members")
    @SendToUser("/sub/{roomId}")
    public Flux<ChatSubscriber> getChatGroup(@DestinationVariable String roomId) {
        return chatService.loadChatMembers(roomId);
    }

    // todo: 이거 없어도 될거같음
    @MessageMapping("/{roomId}/info")
    @SendToUser("/sub/{roomId}")
    public Mono<ChatInfo> getChatInfo(@DestinationVariable String roomId) {
        return chatService.loadChatInfo(roomId);
    }

    //todo: 헤더랑 공지사항 로드하는 거 하나 만들어야함

    @MessageMapping("/{roomId}/role")
    @SendTo("/sub/{roomId}")
    public Mono<RoleChange> changeRole(
            @DestinationVariable String roomId,
            @Payload RolePayload rolePayload,
            Principal user
    ) {
        StompUser stompUser = (StompUser) user;
        return chatService.modifyRole(roomId, rolePayload.getMemberId(), rolePayload.getTo(), stompUser.getMemberId());
    }

    @MessageMapping("/{roomId}/modify/{messageId}")
    @SendTo("/sub/{roomId}")
    public Mono<ChatMessageDto> changeMessage(
            @DestinationVariable String roomId,
            @DestinationVariable String messageId,
            @Payload MessagePayload subject,
            Principal user
    ) {
        StompUser stompUser = (StompUser) user;
        return chatService.modifyMessage(messageId, stompUser.getMemberId(), subject);
    }

    @MessageMapping("/{roomId}/delete/{messageId}")
    @SendTo("/sub/{roomId}")
    public Mono<DeletedMessage> deleteMessage(
            @DestinationVariable String roomId,
            @DestinationVariable String messageId,
            Principal user
    ) {
        StompUser stompUser = (StompUser) user;
        return chatService.deleteMessage(messageId, stompUser.getMemberId());
    }
}
