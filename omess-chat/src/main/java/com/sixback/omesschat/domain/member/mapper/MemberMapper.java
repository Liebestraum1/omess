package com.sixback.omesschat.domain.member.mapper;

import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import com.sixback.omesschat.domain.member.model.entity.Member;

public class MemberMapper {

    public static MemberInfo toMemberInfo(Member member) {
        return new MemberInfo(member.getId(), member.getNickname(), member.getEmail());
    }
}

