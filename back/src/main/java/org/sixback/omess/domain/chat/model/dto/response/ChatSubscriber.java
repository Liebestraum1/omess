package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatSubscriber {

    private ResponseType type = ResponseType.MEMBER;
    private Long memberId;
    private String email;
    private String nickname;
    private String profile;

    protected ChatSubscriber() {
    }

    @Builder
    public ChatSubscriber(Long memberId, String email, String nickname, String profile) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }
}
