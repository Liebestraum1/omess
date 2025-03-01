package org.sixback.omess.domain.file.model.dto.response;

import lombok.Builder;
import org.sixback.omess.domain.file.model.enums.ReferenceType;

@Builder
public record UploadFileResponse(
        Long id,
        String originalName,
        String address,
        String contentType,
        String referenceId,
        ReferenceType referenceType
) {
}
