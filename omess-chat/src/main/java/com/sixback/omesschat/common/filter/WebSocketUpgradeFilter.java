package com.sixback.omesschat.common.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebSocketUpgradeFilter implements WebFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String upgradeHeader = request.getHeaders().getFirst("Upgrade");
        if ("websocket".equalsIgnoreCase(upgradeHeader)) {
            System.out.println("Handling WebSocket upgrade request");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
