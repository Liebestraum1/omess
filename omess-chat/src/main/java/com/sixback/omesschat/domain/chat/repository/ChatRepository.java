package com.sixback.omesschat.domain.chat.repository;

import com.sixback.omesschat.domain.chat.model.entity.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Query("{ 'projectId': ?0, 'members': { '$elemMatch': { 'memberId': ?1, 'isAlive': true } } }")
    Flux<Chat> findByProjectIdAndMemberId(Long projectId, Long memberId);

}
