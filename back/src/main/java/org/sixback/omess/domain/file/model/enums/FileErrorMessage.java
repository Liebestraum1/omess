package org.sixback.omess.domain.file.model.enums;

import lombok.Getter;

@Getter
public enum FileErrorMessage {
    FILE_INFO_NOT_FOUND_ERROR("파일 정보가 존재하지 않습니다."),
    MAX_UPLOAD_SIZE_EXCEEDED_ERROR("파일 업로드 한계 용량을 초과 하였습니다.");

    FileErrorMessage(String message) {
        this.message = message;
    }

    private final String message;
}
