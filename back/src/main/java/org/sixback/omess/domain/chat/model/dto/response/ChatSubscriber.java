package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatSubscriber {

    private String email;
    private String nickname;
    private String profile;
}
