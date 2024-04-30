package org.sixback.omess.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    INCOMPLETE_REQUEST_BODY_ERROR("요청에 필요한 모든 데이터가 포함되지 않았습니다."),
    VALIDATION_ERROR("유효한 요청이 아닙니다."),
    UNAUTHENTICATED_ERROR("인증에 실패하였습니다."),
    NEED_AUTHENTICATION_ERROR("인증된 사용자여야 합니다."),
    UNKNOWN_ERROR("서버에 알 수 없는 에러가 발생하였습니다."),
    NOT_FOUND_ERROR("요청하신 data를 찾을 수 없습니다.");

    private final String title;

    ErrorType(String title) {
        this.title = title;
    }
}