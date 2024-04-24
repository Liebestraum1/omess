package org.sixback.omess.domain.member.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.sixback.omess.domain.member.model.dto.request.MemberNicknameCheckResponse;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public ResponseEntity<MemberEmailCheckResponse> isExistEmail(
            @Email
            @Length(max = 50)
            @NotBlank
            @Validated
            @RequestParam(name = "email") String email
    ) {
        return ResponseEntity.ok()
                .body(memberService.isExistEmail(email));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<MemberNicknameCheckResponse> isExistNickname(
            @Length(max = 30)
            @NotBlank
            @Validated
            @RequestParam(name = "nickname") String nickname
    ) {
        return ResponseEntity.ok()
                .body(memberService.isExistNickname(nickname));
    }
}
