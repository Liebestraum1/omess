package com.sixback.omesschat.domain.chat.repository;

import com.sixback.omesschat.domain.chat.model.entity.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
}
