package org.sixback.omess.domain.chat.repository;

import org.sixback.omess.domain.chat.model.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Flux<ChatMessage> findChatMessagesByChatId(String chatId);
}
