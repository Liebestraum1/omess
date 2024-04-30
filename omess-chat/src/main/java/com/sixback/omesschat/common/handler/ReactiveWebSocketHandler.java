package com.sixback.omesschat.common.handler;

import com.sixback.omesschat.domain.chat.model.dto.ChatMessageDto;
import com.sixback.omesschat.domain.chat.model.message.EnterMessage;
import com.sixback.omesschat.domain.chat.model.message.Message;
import com.sixback.omesschat.domain.chat.model.message.MessageType;
import com.sixback.omesschat.domain.chat.model.message.SendMessage;
import com.sixback.omesschat.domain.chat.parser.MessageParser;
import com.sixback.omesschat.domain.chat.service.ChatService;
import com.sixback.omesschat.domain.chat.service.WebSocketSessionService;
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

    private final WebSocketSessionService sessionService;
    private final ChatService chatService;

    /**
     * 웹 소켓의 데이터 처리
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> receive = session.receive();
        return receive.doOnSubscribe(message -> log.info("WebSocket 연결"))
                .map(message -> MessageParser.parseMessage(message.getPayloadAsText()))
                .flatMap(message -> process(session, message))
                .flatMap(o -> after(session, o))
                .doOnError((error) -> {
                    error.printStackTrace();
                    log.error("에러 발생 : {} - {}", error.getCause(), error.getMessage());
                })
                .doFinally(type -> sessionService.leave(session))
                .then();
    }

    /**
     * 메시지 처리
     */
    private Mono<?> process(WebSocketSession session, Message message) {
        MessageType type = message.getType();
        return switch (type) {
            case ENTER -> sessionService
                    .enter(session, MessageParser.parseMessage(message.getData(), EnterMessage.class));
            case SEND -> {
                SendMessage sendMessage = MessageParser.parseMessage(message.getData(), SendMessage.class);

                String chatId = sessionService.findChatId(session);
                Long memberId = sessionService.findMemberId(session);

                yield chatService.saveChatMessage(chatId, memberId, sendMessage);
            }
            default -> throw new TypeNotPresentException(type.name(), new Throwable("Not Present MessageType"));
        };
    }

    /**
     * 처리가 완료된 데이터 사용자에게 송신
     */
    private Mono<Void> after(WebSocketSession session, Object data) {
        if (data instanceof ChatMessageDto) {
            return sessionService.send(session, (ChatMessageDto) data);
        }
        throw new ClassCastException();
    }
}
