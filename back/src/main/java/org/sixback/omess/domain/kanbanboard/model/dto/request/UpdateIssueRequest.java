package org.sixback.omess.domain.kanbanboard.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateIssueRequest {
    @NotBlank(message = "title은 공백일 수 없습니다.")
    String title;
    String content;
}
