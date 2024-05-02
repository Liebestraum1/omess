package org.sixback.omess.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.sixback.omess.domain.member.exception.DuplicateEmailException;
import org.sixback.omess.domain.member.exception.DuplicateNicknameException;
import org.sixback.omess.domain.member.model.dto.request.MemberNicknameCheckResponse;
import org.sixback.omess.domain.member.model.dto.request.SignInMemberRequest;
import org.sixback.omess.domain.member.model.dto.request.SignupMemberRequest;
import org.sixback.omess.domain.member.model.dto.response.MemberEmailCheckResponse;
import org.sixback.omess.domain.member.model.dto.response.SignInMemberResponse;
import org.sixback.omess.domain.member.model.dto.response.SignupMemberResponse;
import org.sixback.omess.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
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

    @PostMapping("/signup")
    public ResponseEntity<SignupMemberResponse> signup(
            @Validated
            @RequestBody
            SignupMemberRequest signupMemberRequest
    ) {
        return ResponseEntity.ok()
                .body(memberService.signup(signupMemberRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInMemberResponse> signin(
            @Validated
            @RequestBody
            SignInMemberRequest signInMemberRequest,
            HttpServletRequest request
    ) {
        SignInMemberResponse signinMemberResponse = memberService.signin(signInMemberRequest);

        HttpSession session = request.getSession();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        session.setAttribute("memberId", signinMemberResponse.memberId());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(signinMemberResponse.memberId(), null, authorities);
        securityContext.setAuthentication(authentication);

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return ResponseEntity.ok()
                .body(signinMemberResponse);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateNicknameException.class})
    public ErrorResponse DuplicateExceptionHandler(
            HttpServletRequest request, RuntimeException exception
    ) {
        log.error("{} 발생: {}", exception.getClass(), exception.getMessage());
        return ErrorResponse.builder(exception, BAD_REQUEST, exception.getMessage())
                .title(exception.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }
}
