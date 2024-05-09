package org.sixback.omess.domain.kanbanboard.model.dto.request.issue;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateIssueCommonRequest {
    Long chargerId;
    Long labelId;
    @Min(0) @Max(3)
    Integer importance;
    @Min(1) @Max(3)
    Integer status;
}
