package org.sixback.omess.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sixback.omess.domain.member.model.dto.request.SignInMemberRequest;
import org.sixback.omess.domain.member.model.dto.request.SignupMemberRequest;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.sixback.omess.common.TestUtils.makeMember;
import static org.sixback.omess.common.exception.ErrorType.*;
import static org.sixback.omess.domain.member.exception.MemberErrorMessage.DUPLICATE_EMAIL;
import static org.sixback.omess.domain.member.exception.MemberErrorMessage.DUPLICATE_NICKNAME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("checkEmail Test")
    @Transactional
    class CheckEmailTest {
        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 성공 - 존재함")
        void isExistEmail_success_Exist() throws Exception {
            Member savdMember = memberRepository.save(makeMember());

            mockMvc.perform(get("/api/v1/members/check-email")
                            .param("email", savdMember.getEmail()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("true")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 성공 - 존재하지 않음")
        void isExistEmail_success_NotExist() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-email")
                            .param("email", "test@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("false")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 실패 - 존재하지 않음")
        void isExistEmail_fail_noEmail() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-email"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/check-email"))
                    .andDo(print())
            ;
        }

        @DisplayName("해당하는 email을 가진 member가 있는지 조회 실패 - email validation 실패")
        @ValueSource(strings = {"", "a", "1234567890_1234567890_1234567890_1234567890_1234567890_"})
        @ParameterizedTest
        void isExistEmail_fail_nickname_validation(String email) throws Exception {
            mockMvc.perform(get("/api/v1/members/check-email")
                            .param("email", email))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/check-email"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("checkNickname Test")
    @Transactional
    class CheckNicknameTest {
        @Test
        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 성공 - 존재함")
        void isExistNickname_success_exist() throws Exception {
            Member savedMember = makeMember();
            memberRepository.save(savedMember);

            mockMvc.perform(get("/api/v1/members/check-nickname")
                            .param("nickname", savedMember.getNickname()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("true")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 성공 - 존재하지 않음")
        void isExistNickname_success_notExist() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-nickname")
                            .param("nickname", "noNick"))
                    .andExpect(status().isOk())
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 실패 - nickname 없음")
        void isExistNickname_fail_noNickname() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-nickname"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/check-nickname"))
                    .andDo(print())
            ;
        }

        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 실패 - nickname validation 실패")
        @ValueSource(strings = {"", "1234567890_1234567890_1234567890_1"})
        @ParameterizedTest
        void isExistNickname_fail_validation(String nickname) throws Exception {
            mockMvc.perform(get("/api/v1/members/check-nickname")
                            .param("nickname", nickname))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/check-nickname"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    @Transactional
    class SignupTest {
        @Test
        @DisplayName("회원가입 성공")
        void signup_success() throws Exception {
            String signupMemberRequest = objectMapper.writeValueAsString(new SignupMemberRequest(
                    "nickname", "email@naver.com", "password"));

            mockMvc.perform(post("/api/v1/members/signup")
                            .contentType(APPLICATION_JSON)
                            .content(signupMemberRequest)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId").isNumber())
                    .andDo(print())
            ;
        }

        @DisplayName("회원가입 실패 - validation 실패")
        @CsvSource(value = {
                " : : ", // all: NOT_BLANK
                "1234567890_1234567890_1234567890_:1234567890_1234567890_1234567890_1234567890_1234567890_:1234567890_1234567890_", // all: MAX_LENGTH,
                "nickname:email@naver.com:pass", // password: MIN_LENGTH
                "nickname:noEmail:password", // email: EMAIL
        }, delimiter = ':'
        )
        @ParameterizedTest
        void signup_fail_validation(String email, String nickname, String password) throws Exception {
            String signupMemberRequest = objectMapper.writeValueAsString(new SignupMemberRequest(email, nickname, password));

            mockMvc.perform(post("/api/v1/members/signup")
                            .contentType(APPLICATION_JSON)
                            .content(signupMemberRequest)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.detail").isString())
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signup"))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("회원가입 실패 - 빈 바디")
        void signup_fail_noBody() throws Exception {
            mockMvc.perform(post("/api/v1/members/signup")
                            .contentType(APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signup"))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("회원가입 - 이미 존재하는 이메일으로 인한 실패")
        void signup_exist_email_fail() throws Exception {
            // given
            Member havingExistEmailMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(havingExistEmailMember);

            String signupMemberRequest = objectMapper.writeValueAsString(new SignupMemberRequest(
                    "nickname1", "email@naver.com", "password"));

            // when
            mockMvc.perform(post("/api/v1/members/signup")
                            .contentType(APPLICATION_JSON)
                            .content(signupMemberRequest))
                    //then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(DUPLICATE_EMAIL.getMessage()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signup"))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("회원가입 - 이미 존재하는 닉네임으로 인한 실패")
        void signup_exist_nickname_fail() throws Exception {
            // given
            Member havingExistNicknameMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(havingExistNicknameMember);

            String signupMemberRequest = objectMapper.writeValueAsString(new SignupMemberRequest(
                    "nickname", "email1@naver.com", "password"));

            // when
            mockMvc.perform(post("/api/v1/members/signup")
                            .contentType(APPLICATION_JSON)
                            .content(signupMemberRequest))
                    //then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(DUPLICATE_NICKNAME.getMessage()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signup"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    @Transactional
    class SigninTest {
        @Test
        @DisplayName("로그인 - 성공")
        void signin_success() throws Exception {
            // given
            Member member = makeMember("nickname", "email@naver.com", "password");
            Member savedMember = memberRepository.save(member);

            String signinMemberRequest = objectMapper.writeValueAsString(
                    new SignInMemberRequest("email@naver.com", "password")
            );

            // when
            mockMvc.perform(post("/api/v1/members/signin")
                            .contentType(APPLICATION_JSON)
                            .content(signinMemberRequest))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId").value(savedMember.getId()))
                    .andExpect(jsonPath("$.nickname").value(savedMember.getNickname()))
//                    .andExpect(request().sessionAttribute("memberId", is(notNullValue())))
//                    .andExpect(request().sessionAttribute("SPRING_SECURITY_CONTEXT", is(notNullValue())))
                    .andDo(print());
        }

        @Test
        @DisplayName("로그인 - 빈바디 실패")
        void signin_fail() throws Exception {
            // when
            mockMvc.perform(post("/api/v1/members/signin")
                            .contentType(APPLICATION_JSON))
                    //then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signin"))
                    .andDo(print());
        }

        @CsvSource(value = {
                "notSaved:email@naver.com:password:password:", // email에 해당하는 유저가 존재하지 않음
                "nickname:email@naver.com:password:failPassword:", // 비밀번호 불일치
        }, delimiter = ':')
        @DisplayName("로그인 - 인증 실패")
        @ParameterizedTest
        void signin_fail_unauthenticated(String nickname, String email, String realPassword, String inputPassword) throws Exception {
            if (!nickname.equals("notSaved")) {
                Member member = makeMember(nickname, email, realPassword);
                memberRepository.save(member);
            }

            String signinMemberRequest = objectMapper.writeValueAsString(
                    new SignInMemberRequest(email, inputPassword)
            );

            // when
            mockMvc.perform(post("/api/v1/members/signin")
                                    .contentType(APPLICATION_JSON)
                                    .content(signinMemberRequest)
                            //then
                    )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(UNAUTHENTICATED_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signin"))
                    .andDo(print());
        }
    }

    @Nested
    class SignoutTest {
        @Test
        @DisplayName("로그아웃 - 성공")
        void signin_success() throws Exception {
            Member member = makeMember("nickname", "e@naver.com", "password123");
            memberRepository.save(member);
            Cookie cookie = getCookie("e@naver.com", "password123");

            // when
            mockMvc.perform(post("/api/v1/members/signout")
                            .contentType(APPLICATION_JSON)
                            .cookie(cookie))
                    //then
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("로그아웃 - 로그인하지 않았던 사용자가 로그아웃 요청 실패")
        void signin_fail_notSignin() throws Exception {
            // when
            mockMvc.perform(post("/api/v1/members/signout")
                            .contentType(APPLICATION_JSON))
                    //then
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signout"))
                    .andDo(print());
        }
    }

    private Cookie getCookie(String email, String password) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/members/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignInMemberRequest(email, password))))
                .andReturn()
                .getResponse();
        return response.getCookie("SESSION");
    }
}