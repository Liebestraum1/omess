package org.sixback.omess.domain.member.model.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;


@Getter
@ToString
@RequiredArgsConstructor
public class UpdateMemberRequest {
    @Length(min = 8, max = 20)
    private final String password;

    private final MultipartFile profile;
}