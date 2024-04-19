package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sixback.omess.domain.chat.model.entity.ChatRole;

@Builder
@Getter
public class RoleChange {

    private ChatSubscriber target;
    private ChatRole to;
}
