package org.sixback.omess.domain.apispecification.exception;

import lombok.Getter;

@Getter
public enum ApiSpecificationErrorMessage {
    API_SPECIFICATION_NOT_FOUND("해당하는 API 명세서가 존재하지 않습니다."),
    PATH_MISMATCH("존재하지 않는 경로입니다.");

    ApiSpecificationErrorMessage(String message) {
        this.message = message;
    }

    private final String message;
}
