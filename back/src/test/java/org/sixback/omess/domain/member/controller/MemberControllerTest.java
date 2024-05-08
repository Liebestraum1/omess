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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sixback.omess.common.TestUtils.makeMember;
import static org.sixback.omess.common.exception.ErrorType.*;
import static org.sixback.omess.common.utils.PasswordUtils.isNotValidPassword;
import static org.sixback.omess.domain.member.exception.MemberErrorMessage.DUPLICATE_EMAIL;
import static org.sixback.omess.domain.member.exception.MemberErrorMessage.DUPLICATE_NICKNAME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        }, delimiter = ':')
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
    @DisplayName("로그아웃 테스트")
    class SignoutTest {
        @Test
        @DisplayName("로그아웃 - 성공")
        void signin_success() throws Exception {
            Member member = makeMember("nickname", "e@naver.com", "password123");
            memberRepository.save(member);
            Cookie cookie = getSessionCookie("e@naver.com", "password123");

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

    @Nested
    @DisplayName("회원 조회 테스트")
    class searchMemberTest {
        @Test
        @DisplayName("사용자 사용자 아이디 하나만으로 조회 성공")
        void searchMember_noArgument_success() throws Exception {
            // given
            List<Member> members = new ArrayList<>();
            Member member1 = makeMember("nickname1", "email1@naver.com", "password123");
            Member member2 = makeMember("nickname2", "email2@naver.com", "password123");
            Member member3 = makeMember("nickname3", "email3@naver.com", "password123");
            members.add(member1);
            members.add(member2);
            members.add(member3);
            memberRepository.saveAll(members);

            Cookie cookie = getSessionCookie("email1@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(3))
                    .andDo(print());
        }

        @CsvSource(value = {
                "memberId:0:1", "memberId:1:1", "memberId:-1:0", "memberId:-1:0"}, delimiter = ':')
        @ParameterizedTest
        @DisplayName("사용자 사용자 아이디 하나만으로 조회 성공")
        void searchMember_byOnlyMemberId_success(String paramName, int idx, int expect) throws Exception {
            // given
            List<Member> members = new ArrayList<>();
            Member member1 = makeMember("nickname1", "email1@naver.com", "password123");
            Member member2 = makeMember("nickname2", "email2@naver.com", "password123");
            Member member3 = makeMember("nickname3", "email3@naver.com", "password123");
            members.add(member1);
            members.add(member2);
            members.add(member3);
            memberRepository.saveAll(members);
            Long id;
            if (idx == -1) {
                id = -1L;
            } else {
                id = members.get(idx).getId();
            }

            Cookie cookie = getSessionCookie("email1@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param(paramName, String.valueOf(id))
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(expect))
                    .andDo(print());
        }

        @CsvSource(value = {
                "nickname:nick:3", "nickname:nickname1:1", "nickname:notMatched:0", "nickname:nickname0:0",
                "email:email:3", "email:email1:1", "email:notMatched:0", "email:email@naver.com0:0"
        }, delimiter = ':')
        @ParameterizedTest
        @DisplayName("사용자 닉네임만으로 조회 성공")
        void searchMember_byOnlyNickname_success(String paramName, String value, int expect) throws Exception {
            // given
            List<Member> members = new ArrayList<>();
            Member member1 = makeMember("nickname1", "email1@naver.com", "password123");
            Member member2 = makeMember("nickname2", "email2@naver.com", "password123");
            Member member3 = makeMember("nickname3", "email3@naver.com", "password123");

            members.add(member1);
            members.add(member2);
            members.add(member3);
            memberRepository.saveAll(members);
            Cookie cookie = getSessionCookie("email1@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param(paramName, value)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(expect))
                    .andDo(print());
        }

        @CsvSource(value = {"email:3", "email1:1", "notMatched:0", "email@naver.com0:0"}, delimiter = ':')
        @ParameterizedTest
        @DisplayName("사용자 이메일으로만 조회 성공")
        void searchMember_byOnlyEmail_success(String nickname, int expect) throws Exception {
            // given
            List<Member> members = new ArrayList<>();
            Member member1 = makeMember("nickname1", "email1@naver.com", "password123");
            Member member2 = makeMember("nickname2", "email2@naver.com", "password123");
            Member member3 = makeMember("nickname3", "email3@naver.com", "password123");

            members.add(member1);
            members.add(member2);
            members.add(member3);
            memberRepository.saveAll(members);
            Cookie cookie = getSessionCookie("email1@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param("email", nickname)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(expect))
                    .andDo(print());
        }

        @CsvSource(value = {"nickname1:email1:1", "nickname1:notMatched:0", "notMatched:email:0", "nickname1:email3@naver.com:0"}, delimiter = ':')
        @ParameterizedTest
        @DisplayName("사용자 닉네임과 email으로 조회 성공")
        void searchMember_byEmailAndNickname_success(String nickname, String email, int expect) throws Exception {
            // given
            List<Member> members = new ArrayList<>();
            Member member1 = makeMember("nickname1", "email1@naver.com", "password123");
            Member member2 = makeMember("nickname2", "email2@naver.com", "password123");
            Member member3 = makeMember("nickname3", "email3@naver.com", "password123");

            members.add(member1);
            members.add(member2);
            members.add(member3);
            memberRepository.saveAll(members);
            Cookie cookie = getSessionCookie("email1@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param("nickname", nickname)
                            .param("email", email)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(expect))
                    .andDo(print());
        }

        @Test
        @DisplayName("인증이 안된 사용자 조회 실패")
        void searchMember_unAuthorized_fail() throws Exception {
            // given
            memberRepository.save(makeMember());

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param("nickname", "nickname")
                            .param("email", "email"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members"))
                    .andDo(print());
        }

        @CsvSource(
                value = {"nickname:1234567890_1234567890_1234567890_", "email:1234567890_1234567890_1234567890_1234567890_1234567890_"},
                delimiter = ':'
        )
        @ParameterizedTest
        @DisplayName("인수 검증 통과 실패")
        void searchMember_validation_fail(String paramName, String value) throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);
            Cookie cookie = getSessionCookie("email@naver.com", "password123");

            // when
            mockMvc.perform(get("/api/v1/members")
                            .param(paramName, value)
                            .cookie(cookie))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("instance").value("/api/v1/members"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("사용자 수정 테스트")
    class UpdateTest {
        @Test
        @DisplayName("비밀번호만 수정 - 성공")
        void updateMember_OnlyUpdatePassword_success() throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);
            Cookie cookie = getSessionCookie("email@naver.com", "password123");

            // when
            mockMvc.perform(multipart("/api/v1/members")
                            .param("password", "password1234")
                            .contentType(MULTIPART_FORM_DATA)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andDo(print());

            assertThat(isNotValidPassword("password1234", savedMember.getPassword())).isFalse();
            assertThat(isNotValidPassword("password123", savedMember.getPassword())).isTrue();
        }

        @Test
        @DisplayName("프로필 이미지만 수정 - 성공")
        void updateMember_OnlyUpdateProfile_success() throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);
            Cookie cookie = getSessionCookie("email@naver.com", "password123");

            // when
            MockMultipartFile profile = new MockMultipartFile("profile", "profile", "image/jpeg", "image".getBytes());
            mockMvc.perform(multipart("/api/v1/members")
                            .file(profile)
                            .contentType(MULTIPART_FORM_DATA)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andDo(print());
            assertThat(savedMember.getProfile()).isNotBlank();
        }

        @Test
        @DisplayName("비밀번호, 프로필 함께 수정 - 성공")
        void updateMember_UpdatePasswordAndProfile_success() throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);
            Cookie cookie = getSessionCookie("email@naver.com", "password123");

            // when
            MockMultipartFile profile = new MockMultipartFile("profile", "profile", "image/jpeg", "image".getBytes());
            mockMvc.perform(multipart("/api/v1/members")
                            .file(profile)
                            .param("password", "password1234")
                            .contentType(MULTIPART_FORM_DATA)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andDo(print());
            assertThat(isNotValidPassword("password1234", savedMember.getPassword())).isFalse();
            assertThat(isNotValidPassword("password123", savedMember.getPassword())).isTrue();
            assertThat(savedMember.getProfile()).isNotBlank();
        }

        @Test
        @DisplayName("인증 - 실패")
        void updateMember_unAuthorized_fail() throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);

            // when
            MockMultipartFile profile = new MockMultipartFile("profile", "profile", "image/jpeg", "image".getBytes());
            mockMvc.perform(multipart("/api/v1/members")
                            .file(profile)
                            .param("password", "password1234")
                            .contentType(MULTIPART_FORM_DATA))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("status").value(UNAUTHORIZED.value()))
                    .andExpect(jsonPath("title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("instance").value("/api/v1/members"))
                    .andDo(print());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234567", "1234567890_1234567890_", ""})
        @DisplayName("검증 통과 - 실패")
        void updateMember_validation_fail(String password) throws Exception {
            // given
            Member savedMember = makeMember("nickname", "email@naver.com", "password123");
            memberRepository.save(savedMember);
            Cookie cookie = getSessionCookie("email@naver.com", "password123");

            // when
            MockMultipartFile profile = new MockMultipartFile("profile", "profile", "image/jpeg", "image".getBytes());
            mockMvc.perform(multipart("/api/v1/members")
                            .file(profile)
                            .param("password", password)
                            .contentType(MULTIPART_FORM_DATA)
                            .cookie(cookie))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("instance").value("/api/v1/members"))
                    .andDo(print());
        }

    }

    private Cookie getSessionCookie(String email, String password) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/members/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignInMemberRequest(email, password))))
                .andReturn()
                .getResponse();
        return response.getCookie("SESSION");
    }
}