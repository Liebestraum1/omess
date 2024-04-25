package org.sixback.omess.domain.kanbanboard.model.dto.request.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WriteIssueRequest {
    @NotBlank(message = "title은 공백일 수 없습니다.")
    String title;
    String content;
    @NotNull(message = "importance는 공백일 수 없습니다.")
    Integer importance;
    @NotNull(message = "status는 공백일 수 없습니다.")
    Integer status;
    Long memberId;
    Long labelId;
}
