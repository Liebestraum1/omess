package org.sixback.omess.domain.chat.repository;

import org.sixback.omess.domain.chat.model.entity.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
}
