package org.sixback.omess.domain.chat.model;

import org.springframework.web.reactive.socket.WebSocketSession;

public class ChatUser implements ChatObserve {
    private WebSocketSession session;

    @Override
    public void update(String message) {

    }
}
