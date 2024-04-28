package org.sixback.omess.domain.apispecification.exception;

import lombok.Getter;

@Getter
public enum ApiSpecificationErrorMessage {
    API_SPECIFICATION_NOT_FOUND("해당하는 API 명세서가 존재하지 않습니다."),
    PATH_MISMATCH("존재하지 않는 경로입니다."),
    INVALID_JSON("JSON 형식이 아닌 문자열입니다."),
    INVALID_JSON_SCHEMA("JSON Schema Draft7에 부합하지 않는 JSON Schema 구성입니다.");

    ApiSpecificationErrorMessage(String message) {
        this.message = message;
    }

    private final String message;
}
