package com.sixback.omesschat.common.filter;

import com.sixback.omesschat.common.utils.Base64Util;
import com.sixback.omesschat.domain.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class HttpCookieFilter implements WebFilter, Ordered {

    private final SessionRepository sessionRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        if (cookies.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String session = Optional.ofNullable(cookies.get("SESSION")).orElseThrow().getFirst().getValue();
        String sessionId = Base64Util.decode(session);

        return sessionRepository
                .findMemberIdBySessionId(sessionId)
                .doOnNext(memberId -> exchange.getAttributes().put("memberId", memberId))
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
