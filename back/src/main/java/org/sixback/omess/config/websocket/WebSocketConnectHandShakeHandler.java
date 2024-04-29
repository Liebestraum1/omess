package org.sixback.omess.config.websocket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
public class WebSocketConnectHandShakeHandler extends DefaultHandshakeHandler {
    private final String MEMBER_ATTR = "memberId";

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        try {
            HttpSession session = httpRequest.getServletRequest().getSession(false);
            Long memberId = (Long) session.getAttribute(MEMBER_ATTR);
            log.info("memberId : {}", memberId);
        } catch (Exception e) {
            log.error("not exist Session attr");
        }
        return new StompUser(1L);
    }
}
