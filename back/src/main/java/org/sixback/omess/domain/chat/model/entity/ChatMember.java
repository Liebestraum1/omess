package org.sixback.omess.domain.chat.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMember {
    private Long memberId;
    @Setter
    private ChatRole role;
    @Setter
    private Boolean isAlive = true;

    @Builder
    public ChatMember(Long memberId, ChatRole role) {
        this.memberId = memberId;
        this.role = role;
    }
}
