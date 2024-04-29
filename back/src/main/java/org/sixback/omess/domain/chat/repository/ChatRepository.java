package org.sixback.omess.domain.chat.repository;


import org.sixback.omess.domain.chat.model.entity.Chat;
import org.sixback.omess.domain.chat.model.entity.ChatMember;
import org.sixback.omess.domain.chat.model.entity.Content;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Query("{'projectId': ?0, 'members.memberId': ?1, 'members.isAlive': true}")
    Flux<Chat> findAllByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query(value = "{ chatId: ?0, members: {$match: {isAlive: true}}}", fields = "{members:  1}")
    Flux<ChatMember> findMembersByChatId(String chatId);
}
