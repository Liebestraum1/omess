package org.sixback.omess.websocket;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketConnectionTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient client;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @BeforeEach
    public void setup() {
        this.client = new WebSocketStompClient(new StandardWebSocketClient());
        this.client.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void verifyEchoMessage() throws Exception {
        String echoMessage = "Hello, world!";
        StompSession session = client
                .connect(String.format("ws://localhost:%d/chat/v1", port), headers, new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        log.info("WebSocket Connected");
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        assertEquals(echoMessage, payload.toString());
                    }
                })
                .get(10, SECONDS);

        Assertions.assertThat(session.isConnected()).isEqualTo(true);
    }
}
