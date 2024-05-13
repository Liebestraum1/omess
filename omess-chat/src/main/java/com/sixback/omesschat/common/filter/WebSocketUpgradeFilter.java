package com.sixback.omesschat.common.filter;

import com.sixback.omesschat.domain.chat.service.WebSocketSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class WebSocketUpgradeFilter implements WebFilter, Ordered {

    private final WebSocketSessionService webSocketSessionService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        Long memberId = exchange.getAttribute("memberId");
        String upgradeHeader = request.getHeaders().getFirst("Upgrade");

        if (memberId == null && upgradeHeader == null) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        if ("websocket".equalsIgnoreCase(upgradeHeader)) {
            System.out.println("Handling WebSocket upgrade request");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
