package org.sixback.omess.domain.chat.model.dto.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.sixback.omess.domain.chat.model.entity.ChatRole;

@Getter
public class RolePayload {

    @NotBlank(message = "대상을 지정하세요.")
    private Long memberId;
    @NotNull(message = "변경할 권한을 지정하세요.")
    private ChatRole to;
}
