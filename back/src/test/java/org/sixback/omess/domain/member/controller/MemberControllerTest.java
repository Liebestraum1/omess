package org.sixback.omess.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sixback.omess.domain.member.model.dto.request.SignupMemberRequest;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.sixback.omess.common.TestUtils.makeMember;
import static org.sixback.omess.common.exception.ErrorType.INCOMPLETE_REQUEST_BODY_ERROR;
import static org.sixback.omess.common.exception.ErrorType.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("checkEmail Test")
    @Transactional
    class CheckEmailTest {
        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 성공 - 존재함")
        void isExistEmail_success_Exist() throws Exception {
            Member savdMember = memberRepository.save(makeMember());

            mockMvc.perform(get("/api/v1/members/check-email")
                            .param("email", savdMember.getEmail())
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("true")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 성공 - 존재하지 않음")
        void isExistEmail_success_NotExist() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-email")
                            .param("email", "test@email.com")
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("false")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 email을 가진 member가 있는지 조회 실패 - 존재하지 않음")
        void isExistEmail_fail_noEmail() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-email")
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
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
                            .param("email", email)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
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
                            .param("nickname", savedMember.getNickname())
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.isExist").value(("true")))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 성공 - 존재하지 않음")
        void isExistNickname_success_notExist() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-nickname")
                            .param("nickname", "noNick")
                    ).andExpect(status().isOk())
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("해당하는 nickname을 가진 member가 있는지 조회 실패 - nickname 없음")
        void isExistNickname_fail_noNickname() throws Exception {
            mockMvc.perform(get("/api/v1/members/check-nickname")
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
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
                            .param("nickname", nickname)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/check-nickname"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("checkEmail Test")
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
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
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
                    .andExpect(jsonPath("$.type").value(INCOMPLETE_REQUEST_BODY_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/members/signup"))
                    .andDo(print())
            ;
        }
    }
}