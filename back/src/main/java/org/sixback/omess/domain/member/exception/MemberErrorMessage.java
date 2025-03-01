package org.sixback.omess.domain.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorMessage {
    DUPLICATE_EMAIL("중복된 email입니다"),
    DUPLICATE_NICKNAME("중복된 nickname입니다."),
    MEMBER_NOT_FOUND("해당하는 member가 존재하지 않습니다."),
    MEMBER_AUTHENTICATION_FAIL("member 인증 실패");

    MemberErrorMessage(String message) {
        this.message = message;
    }

    private final String message;
}
