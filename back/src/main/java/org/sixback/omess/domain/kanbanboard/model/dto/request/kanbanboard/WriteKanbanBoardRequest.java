package org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WriteKanbanBoardRequest {
    @NotEmpty
    @Size(min = 1, max = 90)
    String name;

    @NotEmpty
    @Size(min = 1, max = 20)
    String category;
}
