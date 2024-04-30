package com.sixback.omesschat.common.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> receive = session.receive();

        log.info("여기까지 안오나????");
        receive.subscribe(
                message -> {
                    log.info("receive message {}", message.getPayload());
                },
                error -> {
                    log.error("ERROR : {}", error.getMessage());
                    error.printStackTrace();
                },
                () -> {
                    log.info("WebSocket Connect !!!");
                }
        );

        return Mono.empty();
    }

}
