package com.sixback.omesschat.domain.member.mapper;

import com.sixback.omesschat.domain.file.model.dto.response.FileDto;
import com.sixback.omesschat.domain.member.model.dto.MemberInfo;
import com.sixback.omesschat.domain.member.model.entity.Member;
import jakarta.annotation.Nullable;

public class MemberMapper {

    public static MemberInfo toMemberInfo(Member member, @Nullable FileDto fileDto) {
        String fileUrl = (fileDto != null) ? fileDto.getUrl() : null;
        return new MemberInfo(member.getId(), fileUrl, member.getNickname(), member.getEmail());
    }
}

