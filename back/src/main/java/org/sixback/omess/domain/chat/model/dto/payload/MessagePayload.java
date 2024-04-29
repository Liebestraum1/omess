package org.sixback.omess.domain.chat.model.dto.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.sixback.omess.domain.chat.model.entity.MessageType;

@Getter
public class MessagePayload {

    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 16383)
    private String subject;
    @NotNull(message = "메시지 타입을 입력하세요.")
    private MessageType messageType;
}
