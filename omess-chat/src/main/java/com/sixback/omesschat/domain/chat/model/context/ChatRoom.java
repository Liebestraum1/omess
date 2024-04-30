package com.sixback.omesschat.domain.chat.model.context;

import lombok.Getter;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ChatRoom {

    private String chatId;

    private final List<ChatUser> participants = new ArrayList<>();

    private ChatRoom() {
    }

    private ChatRoom(String chatId) {
        this.chatId = chatId;
    }

    public static ChatRoom of(String chatId) {
        return new ChatRoom(chatId);
    }

    public void register(ChatUser user) {
        participants.add(user);
    }

    public Mono<Boolean> remove(ChatUser user) {
        participants.remove(user);
        return Mono.just(participants.isEmpty());
    }

    public Mono<Void> notify(String message) {
        return Flux.fromIterable(participants)
                .flatMap(participant -> participant.update(message))
                .then();
    }

    public ChatUser findChatUser(WebSocketSession session) {
        return participants.stream().filter(s -> s.getSession().equals(session)).findAny().orElseThrow();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(chatId, chatRoom.chatId) && Objects.equals(participants, chatRoom.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, participants);
    }
}

