package com.sixback.omesschat.domain.chat.repository;

import com.sixback.omesschat.domain.chat.model.entity.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Query("{projectId: ?0, members: {memberId: ?1, isAlive: true}}")
    Flux<Chat> findByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query("{id: ?0, members:  {memberId: ?1, isAlive: true}}")
    Mono<Chat> findByChatIdAndMemberId(String chatId, Long memberId);
}
