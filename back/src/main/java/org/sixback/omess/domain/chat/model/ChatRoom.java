package org.sixback.omess.domain.chat.model;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements ChatSubject{
    List<ChatObserve> observes = new ArrayList<>();
    @Override
    public void register(ChatObserve chatObserve) {
        observes.add(chatObserve);
    }

    @Override
    public void remove(ChatObserve chatObserve) {
        observes.remove(chatObserve);
    }

    @Override
    public void notify(String message) {
        observes.forEach(chatObserve -> chatObserve.update(message));
    }
}
