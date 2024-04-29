package org.sixback.omess.domain.chat.model;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatManager {

    private final Map<String, Flux<Object>> chatRooms = new ConcurrentHashMap<>();

    public void joinRoom(WebSocketSession session) {

    }
}
