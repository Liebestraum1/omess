package com.sixback.omesschat.domain.chat.repository;

import com.sixback.omesschat.domain.chat.model.entity.ChatMessage;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    @Query("{chatId: ?0, writer: ?1, isDeleted:  false}")
    Flux<ChatMessage> findAllByChatIdAndWriter(String chatId, Long memberId);
}
