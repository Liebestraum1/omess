package com.sixback.omesschat.domain.chat.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMember {

    private Long memberId;

    @Setter
    private boolean isAlive = true;
    @Setter
    private ChatRole role;

    public ChatMember(Long memberId) {
        this.memberId = memberId;
    }
}
