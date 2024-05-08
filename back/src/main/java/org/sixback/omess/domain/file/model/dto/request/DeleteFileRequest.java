package org.sixback.omess.domain.file.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteFileRequest {
    @NotNull
    private final List<Long> deleteIds;

    public DeleteFileRequest(@JsonProperty("deleteIds") List<Long> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
