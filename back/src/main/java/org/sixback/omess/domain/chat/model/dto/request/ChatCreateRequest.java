package org.sixback.omess.domain.chat.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatCreateRequest {
    @Size(min = 2, message = "2명 이상을 선택해 주세요.")
    private List<String> emails;

    @NotBlank
    @Size(min = 1)
    private String name;
}
