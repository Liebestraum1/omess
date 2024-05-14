package com.sixback.omesschat.domain.chat.service;

import com.sixback.omesschat.domain.chat.model.context.ChatRoom;
import com.sixback.omesschat.domain.chat.model.context.ChatUser;
import com.sixback.omesschat.domain.chat.model.dto.request.message.EnterRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseMessage;
import com.sixback.omesschat.domain.chat.parser.MessageParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class WebSocketSessionService {
    private final ConcurrentMap<String, ChatRoom> rooms;
    private final ConcurrentMap<ChatUser, ChatRoom> users;

    public WebSocketSessionService() {
        this.rooms = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }

    public Flux<ResponseMessage> enter(WebSocketSession session, EnterRequestMessage message) {
        log.info("유저 입장... {}", session.getId());
        String chatId = message.getChatId();

        ChatRoom chatRoom = rooms.getOrDefault(chatId, ChatRoom.of(chatId));
        ChatUser chatUser = ChatUser.of(session, message.getMemberId());
        log.info("chatRoom: {}, chatUser: {}", chatRoom.toString(), chatUser.toString());

        ChatRoom room = users.get(chatUser);
        if (room != null) {
            leave(session);
        }
        chatRoom.register(chatUser);
        rooms.put(chatId, chatRoom);
        users.put(chatUser, chatRoom);
        return Flux.just(ResponseMessage.empty());
    }

    public Mono<Void> send(WebSocketSession session, ResponseMessage message) {
        ChatUser chatUser = ChatUser.of(session);
        if (!users.containsKey(chatUser)) {
            throw new IllegalArgumentException();
        }
        ChatRoom chatRoom = users.get(chatUser);

        String response = MessageParser.valueToString(message);
        return chatRoom.notify(response);
    }

    public Mono<Void> sendToUser(WebSocketSession session, ResponseMessage message) {
        String response = MessageParser.valueToString(message);
        Mono<WebSocketMessage> webSocketMessage = Mono.just(session.textMessage(response));
        return session.send(webSocketMessage);
    }

    public void leave(WebSocketSession session) {
        log.info("연결 종료");
        ChatUser chatUser = ChatUser.of(session);
        if (!users.containsKey(chatUser)) {
            throw new IllegalArgumentException();
        }

        ChatRoom chatRoom = users.get(chatUser);
        users.remove(chatUser);
        chatRoom.remove(chatUser)
                .doOnNext(aBoolean -> {
                    if (aBoolean) {
                        rooms.remove(chatRoom.getChatId());
                    }
                }).subscribe();
    }

    public Mono<Long> findMemberId(WebSocketSession session) {
        ChatUser chatUser = ChatUser.of(session);
        ChatRoom chatRoom = users.get(chatUser);
        return Mono.just(chatRoom.findChatUser(session).getMemberId());
    }

    public Mono<String> findChatId(WebSocketSession session) {
        ChatUser chatUser = ChatUser.of(session);
        ChatRoom chatRoom = users.get(chatUser);
        return Mono.just(chatRoom.getChatId());
    }
}
