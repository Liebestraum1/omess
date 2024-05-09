package com.sixback.omesschat.domain.member.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberInfo {

    private Long id;

    private String profile;

    private String nickname;

    private String email;

    public MemberInfo(Long id, String profile, String nickname, String email) {
        this.id = id;
        this.profile = profile;
        this.nickname = nickname;
        this.email = email;
    }
}

