package org.sixback.omess.domain.file.model.dto.response;

import org.sixback.omess.domain.file.model.enums.ReferenceType;

public record GetFileInfoResponse(
        Long id,
        String address,
        ReferenceType referenceType,
        String referenceId
) {
}
