package org.sixback.omess.domain.chat.model;

public interface ChatSubject {
    void register(ChatObserve chatObserve);
    void remove(ChatObserve chatObserve);
    void notify(String message);
}
