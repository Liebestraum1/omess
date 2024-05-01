package com.sixback.omesschat.common.handler;

import static com.sixback.omesschat.domain.chat.model.dto.response.ResponseType.HISTORY;
import static com.sixback.omesschat.domain.chat.model.dto.response.ResponseType.MESSAGE;

import com.sixback.omesschat.domain.chat.model.dto.request.EnterRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.RequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.RequestType;
import com.sixback.omesschat.domain.chat.model.dto.request.SendRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.ResponseMessage;
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
                .flatMap(requestMessage -> process(session, requestMessage))
                .flatMap(o -> after(session, o))
                .doOnError((error) -> error.printStackTrace())
                .doFinally(type -> sessionService.leave(session))
                .then();
    }

    /**
     * 메시지 처리
     */
    private Flux<ResponseMessage> process(WebSocketSession session, RequestMessage requestMessage) {
        log.info("request processing... - {}", session.getId());
        RequestType type = requestMessage.getType();
        return switch (type) {
            case ENTER -> sessionService
                    .enter(session, MessageParser.parseMessage(message.getData(), EnterMessage.class));
            case SEND -> {
                SendRequestMessage sendRequestMessage = MessageParser.parseMessage(requestMessage.getData(),
                        SendRequestMessage.class);

                String chatId = sessionService.findChatId(session);
                Long memberId = sessionService.findMemberId(session);

                yield chatService.saveChatMessage(chatId, memberId, sendRequestMessage);
            }
            default -> throw new TypeNotPresentException(type.name(), new Throwable("Not Present MessageType"));
        };
    }

    /**
     * 처리가 완료된 데이터 사용자에게 응답
     */
    private Mono<Void> after(WebSocketSession session, Object data) {
        if (data instanceof ChatMessageDto) {
            return sessionService.send(session, (ChatMessageDto) data);
        }
        throw new ClassCastException();
    }
}
