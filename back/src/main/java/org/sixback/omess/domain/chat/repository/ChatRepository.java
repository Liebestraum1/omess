package org.sixback.omess.domain.chat.repository;


import org.sixback.omess.domain.chat.model.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {
}
