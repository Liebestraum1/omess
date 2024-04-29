package org.sixback.omess.domain.chat.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMember {

    private Long memberId;
    @Setter
    private boolean isAlive = false;

    public ChatMember(Long memberId) {
        this.memberId = memberId;
    }
}
