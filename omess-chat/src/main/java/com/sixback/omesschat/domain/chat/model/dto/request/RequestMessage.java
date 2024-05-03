package com.sixback.omesschat.domain.chat.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Map;

@Getter
public class RequestMessage {
    @NotNull
    private RequestType type;
    private Map<String, Object> data;
}
