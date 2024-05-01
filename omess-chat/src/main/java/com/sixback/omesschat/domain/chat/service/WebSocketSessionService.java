package com.sixback.omesschat.domain.chat.service;

import com.sixback.omesschat.domain.chat.model.context.ChatRoom;
import com.sixback.omesschat.domain.chat.model.context.ChatUser;
import com.sixback.omesschat.domain.chat.model.dto.request.EnterRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.ResponseMessage;
import com.sixback.omesschat.domain.chat.parser.MessageParser;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WebSocketSessionService {
    private final ConcurrentMap<String, ChatRoom> rooms;
    private final ConcurrentMap<ChatUser, ChatRoom> users;

    public WebSocketSessionService() {
        this.rooms = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }

    public Mono<Void> enter(WebSocketSession session, EnterRequestMessage message) {
        log.info("유저 입장... {}", session.getId());
        String chatId = message.getChatId();

        ChatRoom chatRoom = rooms.getOrDefault(chatId, ChatRoom.of(chatId));
        ChatUser chatUser = ChatUser.of(session, message.getMemberId());

        chatRoom.register(chatUser);
        rooms.put(chatId, chatRoom);
        users.put(chatUser, chatRoom);
        return Mono.empty();
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
        ChatUser chatUser = ChatUser.of(session);
        if (!users.containsKey(chatUser)) {
            throw new IllegalArgumentException();
        }

        ChatRoom chatRoom = users.get(chatUser);
        chatRoom.remove(chatUser)
                .doOnNext(aBoolean -> {
                    users.remove(chatUser);
                    if (aBoolean) {
                        rooms.remove(chatRoom.getChatId());
                    }
                }).subscribe();
    }

    public Long findMemberId(WebSocketSession session) {
        ChatUser chatUser = ChatUser.of(session);
        ChatRoom chatRoom = users.get(chatUser);
        return chatRoom.findChatUser(session).getMemberId();
    }

    public String findChatId(WebSocketSession session) {
        ChatUser chatUser = ChatUser.of(session);
        ChatRoom chatRoom = users.get(chatUser);
        return chatRoom.getChatId();
    }
}
