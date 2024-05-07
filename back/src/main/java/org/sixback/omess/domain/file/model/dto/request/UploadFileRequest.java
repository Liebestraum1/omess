package org.sixback.omess.domain.file.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UploadFileRequest {
    @NotNull
    private final ReferenceType referenceType;

    @NotNull
    private final Long referenceId;

    @NotNull
    private final List<MultipartFile> files;
}
