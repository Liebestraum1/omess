package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sixback.omess.domain.chat.model.entity.ChatRole;

@Getter
public class RoleChange {

    private ResponseType type = ResponseType.ROLE;
    private ChatSubscriber target;
    private ChatRole to;

    @Builder
    public RoleChange(ChatSubscriber target, ChatRole to) {
        this.target = target;
        this.to = to;
    }
}
