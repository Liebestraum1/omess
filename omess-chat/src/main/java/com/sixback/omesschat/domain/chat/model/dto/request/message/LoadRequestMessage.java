package com.sixback.omesschat.domain.chat.model.dto.request.message;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class LoadRequestMessage {
    @Min(value = 0)
    private int offset;
}
