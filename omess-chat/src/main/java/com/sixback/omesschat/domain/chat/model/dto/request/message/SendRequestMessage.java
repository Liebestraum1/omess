package com.sixback.omesschat.domain.chat.model.dto.request.message;

import com.sixback.omesschat.domain.chat.model.entity.ChatFile;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
public class SendRequestMessage {
    @Length(min = 1, max = 16383, message = "메시지 길이를 확인해 주세요.")
    private String message;
    @Size(max = 10)
    private List<ChatFile> files;
}
