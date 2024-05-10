package com.sixback.omesschat.common.filter;

import com.sixback.omesschat.common.utils.Base64Util;
import com.sixback.omesschat.domain.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
public class HttpCookieFilter implements WebFilter, Ordered {

    private final SessionRepository sessionRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        String session = Optional.ofNullable(cookies.get("SESSION")).orElseThrow().getFirst().getValue();
        String sessionId = Base64Util.decode(session);

        return sessionRepository
                .findMemberIdBySessionId(sessionId)
                .doOnNext(memberId -> exchange.getAttributes().put("memberId", memberId))
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
