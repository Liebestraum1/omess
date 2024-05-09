package com.sixback.omesschat.common.handler;

import com.sixback.omesschat.domain.chat.model.dto.request.message.*;
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
                .doOnError(Throwable::printStackTrace)
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
            case LOAD_PIN -> MessageParser.parseMessage(requestMessage.getData(),
                    PinListRequestMessage.class);
            case MEMBERS -> new EmptyRequestMessage(type);
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
            Mono<String> chatIdMono = sessionService.findChatId(session);
            Mono<Long> memberIdMono = sessionService.findMemberId(session);
            return Mono.zip(chatIdMono, memberIdMono)
                    .flatMapMany(tuple -> {
                        String chatId = tuple.getT1();
                        Long memberId = tuple.getT2();
                        return chatWebSocketService.saveChatMessage(chatId, memberId, send);
                    });

        } else if (request instanceof UpdateRequestMessage update) {
            Mono<Long> memberIdMono = sessionService.findMemberId(session);

            return memberIdMono
                    .flatMapMany(memberId ->
                            chatWebSocketService.updateChatMessage(memberId, update.getMessageId(), update.getMessage())
                    );
        } else if (request instanceof DeleteRequestMessage delete) {

            Mono<Long> memberIdMono = sessionService.findMemberId(session);

            return memberIdMono.flatMapMany(memberId ->
                    chatWebSocketService.deleteChatMessage(memberId, delete.getMessageId())
            );
        } else if (request instanceof LoadRequestMessage load) {

            Mono<String> chatIdMono = sessionService.findChatId(session);

            return chatIdMono.flatMapMany(chatId ->
                    chatWebSocketService.loadChatHistory(chatId, load.getOffset())
            );
        } else if (request instanceof PinRequestMessage pin) {

            Mono<Long> memberIdMono = sessionService.findMemberId(session);

            return memberIdMono.flatMapMany(memberId ->
                    chatWebSocketService.pinChatMessage(memberId, pin.getMessageId())
            );
        } else if (request instanceof HeaderRequestMessage header) {

            Mono<String> chatIdMono = sessionService.findChatId(session);
            Mono<Long> memberIdMono = sessionService.findMemberId(session);

            return Mono.zip(chatIdMono, memberIdMono).flatMapMany(tuple -> {
                String chatId = tuple.getT1();
                Long memberId = tuple.getT2();
                return chatWebSocketService.registerHeader(chatId, memberId, header.getDetail());
            });
        } else if (request instanceof ChatNameRequestMessage chatName) {
            Mono<String> chatIdMono = sessionService.findChatId(session);
            Mono<Long> memberIdMono = sessionService.findMemberId(session);

            return Mono.zip(chatIdMono, memberIdMono).flatMapMany(tuple -> {
                String chatId = tuple.getT1();
                Long memberId = tuple.getT2();
                return chatWebSocketService.modifyChatName(chatId, memberId, chatName.getName());
            });
        } else if (request instanceof PinListRequestMessage pinList) {
            int offset = pinList.getOffset();
            Mono<String> chatIdMono = sessionService.findChatId(session);
            return chatIdMono.flatMapMany(chatId ->
                    chatWebSocketService.loadPinMessages(chatId, offset));
        } else if (request instanceof EmptyRequestMessage empty) {
            Mono<String> chatIdMono = sessionService.findChatId(session);
            return chatIdMono.flatMapMany(chatWebSocketService::loadChatMembers);
        }
        throw new TypeNotPresentException("REQUEST", new Throwable("type not present..."));
    }

    /**
     * 처리가 완료된 데이터 사용자에게 응답
     */
    private Mono<Void> after(WebSocketSession session, ResponseMessage message) {
        return switch (message.getType()) {
            case MESSAGE, UPDATE, DELETE, SUCCESS, PIN, HEADER, CHAT_NAME -> sessionService.send(session, message);
            case HISTORY, MEMBERS, LOAD_PIN -> sessionService.sendToUser(session, message);
            default -> Mono.error(new ClassCastException());
        };
    }
}
