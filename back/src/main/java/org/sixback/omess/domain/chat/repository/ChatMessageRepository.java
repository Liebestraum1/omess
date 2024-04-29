package org.sixback.omess.domain.chat.repository;

import org.sixback.omess.domain.chat.model.entity.Chat;
import org.sixback.omess.domain.chat.model.entity.ChatMember;
import org.sixback.omess.domain.chat.model.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
}
