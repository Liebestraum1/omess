package org.sixback.omess.domain.member.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check")
    public ResponseEntity<MemberEmailCheckResponse> isExistEmail(
            @Email
            @NotBlank
            @Validated
            @RequestParam(name = "email") String email
    ) {
        return ResponseEntity.ok()
                .body(memberService.isExistEmail(email));
    }
}
