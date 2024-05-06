package com.sixback.omesschat.common.handler;

import com.sixback.omesschat.domain.chat.model.dto.request.message.ChatNameRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.DeleteRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.EnterRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.HeaderRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.LoadRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.PinRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.RequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.RequestType;
import com.sixback.omesschat.domain.chat.model.dto.request.message.SendRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.request.message.UpdateRequestMessage;
import com.sixback.omesschat.domain.chat.model.dto.response.message.ResponseMessage;
import com.sixback.omesschat.domain.chat.parser.MessageParser;
import com.sixback.omesschat.domain.chat.service.ChatWebSocketService;
import com.sixback.omesschat.domain.chat.service.WebSocketSessionService;
import com.sixback.omesschat.domain.chat.utils.ValidationUtils;
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
    private final ChatWebSocketService chatWebSocketService;

    /**
     * 웹 소켓의 데이터 처리
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> receive = session.receive();
        return receive.doOnSubscribe(message -> log.info("WebSocket 연결"))
                .map(message -> MessageParser.parseMessage(message.getPayloadAsText()))
                .map(this::validate)
                .flatMap(o -> process(session, o))
                .flatMap(o -> after(session, o))
                .doOnError((error) -> error.printStackTrace())
                .doFinally(type -> sessionService.leave(session))
                .then();
    }

    private Object validate(RequestMessage requestMessage) {
        log.info("message validate...");
        ValidationUtils.validate(requestMessage);
        RequestType type = requestMessage.getType();
        Object o = switch (type) {
            case ENTER -> MessageParser.parseMessage(requestMessage.getData(),
                    EnterRequestMessage.class);
            case SEND -> MessageParser.parseMessage(requestMessage.getData(),
                    SendRequestMessage.class);
            case UPDATE -> MessageParser.parseMessage(requestMessage.getData(),
                    UpdateRequestMessage.class);
            case DELETE -> MessageParser.parseMessage(requestMessage.getData(),
                    DeleteRequestMessage.class);
            case LOAD -> MessageParser.parseMessage(requestMessage.getData(),
                    LoadRequestMessage.class);
            case PIN -> MessageParser.parseMessage(requestMessage.getData(),
                    PinRequestMessage.class);
            case HEADER -> MessageParser.parseMessage(requestMessage.getData(),
                    HeaderRequestMessage.class);
            case CHAT_NAME -> MessageParser.parseMessage(requestMessage.getData(),
                    ChatNameRequestMessage.class);
            default -> throw new TypeNotPresentException(type.name(), new Throwable("Not Present MessageType"));
        };
        return ValidationUtils.validate(o);
    }

    /**
     * 메시지 처리
     */
    private Flux<ResponseMessage> process(WebSocketSession session, Object request) {
        log.info("request processing... - {}", session.getId());
        if (request instanceof EnterRequestMessage enter) {
            return sessionService.enter(session, enter);
        } else if (request instanceof SendRequestMessage send) {
            String chatId = sessionService.findChatId(session);
            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.saveChatMessage(chatId, memberId, send);
        } else if (request instanceof UpdateRequestMessage update) {
            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.updateChatMessage(memberId, update.getMessageId(), update.getMessage());
        } else if (request instanceof DeleteRequestMessage delete) {

            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.deleteChatMessage(memberId, delete.getMessageId());
        } else if (request instanceof LoadRequestMessage load) {

            String chatId = sessionService.findChatId(session);

            return chatWebSocketService.loadChatHistory(chatId, load.getOffset());
        } else if (request instanceof PinRequestMessage pin) {

            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.pinChatMessage(memberId, pin.getMessageId());
        } else if (request instanceof HeaderRequestMessage header) {

            String chatId = sessionService.findChatId(session);
            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.registerHeader(chatId, memberId, header.getDetail());
        } else if (request instanceof ChatNameRequestMessage chatName) {

            String chatId = sessionService.findChatId(session);
            Long memberId = sessionService.findMemberId(session);

            return chatWebSocketService.modifyChatName(chatId, memberId, chatName.getName());
        }
        throw new TypeNotPresentException("REQUEST", new Throwable("type not present..."));
    }

    /**
     * 처리가 완료된 데이터 사용자에게 응답
     */
    private Mono<Void> after(WebSocketSession session, ResponseMessage message) {
        return switch (message.getType()) {
            case MESSAGE, UPDATE, DELETE, SUCCESS, PIN, HEADER, CHAT_NAME -> sessionService.send(session, message);
            case HISTORY -> sessionService.sendToUser(session, message);
            default -> Mono.error(new ClassCastException());
        };
    }
}
