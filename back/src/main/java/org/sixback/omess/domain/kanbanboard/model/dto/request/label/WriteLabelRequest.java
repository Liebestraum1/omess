package org.sixback.omess.domain.kanbanboard.model.dto.request.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WriteLabelRequest {
    @NotBlank(message = "라벨은 공백일 수 없습니다.")
    String name;
}
