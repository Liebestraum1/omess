package com.sixback.omesschat.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {

    private final WebSocketHandler handler;

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> handlerMap = Map.of("/chat", handler);
        return new SimpleUrlHandlerMapping(handlerMap, Ordered.HIGHEST_PRECEDENCE);
    }

}
