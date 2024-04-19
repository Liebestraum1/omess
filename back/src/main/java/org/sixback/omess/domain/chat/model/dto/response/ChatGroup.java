package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChatGroup {

    private List<ChatSubscriber> subscribers;
    private int count;
}
