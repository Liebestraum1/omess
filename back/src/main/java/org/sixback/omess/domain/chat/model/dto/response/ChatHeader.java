package org.sixback.omess.domain.chat.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatHeader {

    private ChatSubscriber writer;
    private String header;
    private LocalDateTime time;
}
