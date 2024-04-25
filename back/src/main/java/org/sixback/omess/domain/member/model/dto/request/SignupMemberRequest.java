package org.sixback.omess.domain.member.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignupMemberRequest {
    @NotBlank
    @Length(max = 30)
    private final String nickname;
    @Email
    @NotBlank
    @Length(max = 50)
    private final String email;
    @Length(min = 8, max = 20)
    @NotBlank
    private final String password;

    public SignupMemberRequest(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
