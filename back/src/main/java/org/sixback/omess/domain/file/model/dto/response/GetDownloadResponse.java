package org.sixback.omess.domain.file.model.dto.response;

import org.springframework.core.io.InputStreamResource;

public record GetDownloadResponse(
        Long id,
        String originalName,
        String contentType,
        InputStreamResource data
) {
}
