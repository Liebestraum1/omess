package org.sixback.omess.domain.chat.model.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatInfo {
    private ResponseType type = ResponseType.CHAT_INFO;
    private String id;
    private String name;
    private int messageCount;
    private int memberCount;

    @Builder
    public ChatInfo(String id, String name, int messageCount, int memberCount) {
        this.id = id;
        this.name = name;
        this.messageCount = messageCount;
        this.memberCount = memberCount;
    }
}
