package org.sixback.omess.domain.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorMessage {
    MEMBER_NOT_FOUND("해당하는 member가 존재하지 않습니다.");

    MemberErrorMessage(String message) {
        this.message = message;
    }

    private final String message;
}
