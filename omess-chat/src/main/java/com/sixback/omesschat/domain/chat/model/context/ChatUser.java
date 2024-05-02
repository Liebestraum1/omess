package com.sixback.omesschat.domain.chat.model.context;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Getter
@ToString
public class ChatUser {
    private Long memberId;

    private WebSocketSession session;

    private ChatUser() {}
    private ChatUser(WebSocketSession session) {
        this.session = session;
    }

    private ChatUser(WebSocketSession session, Long memberId) {
        this.session = session;
        this.memberId = memberId;
    }

    public static ChatUser of(WebSocketSession session) {
        return new ChatUser(session);
    }

    public static ChatUser of(WebSocketSession session, Long memberId) {
        return new ChatUser(session, memberId);
    }

    public Mono<Void> update(String message) {
        Mono<WebSocketMessage> webSocketMessage = Mono.just(session.textMessage(message));
        return session.send(webSocketMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser chatUser = (ChatUser) o;
        return Objects.equals(session, chatUser.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}

