package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatInfo {

    private String name;
    private int count;
}
