package org.sixback.omess.domain.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("해당하는 email을 가진 member가 있는지 조회 - 존재함")
    @Transactional
    void isExistEmail_success_Exist() throws Exception {
        memberRepository.save(new Member("nickname", "email@naver.com", "password"));

        mockMvc.perform(get("/api/v1/members/check?email=email@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(("true")))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("해당하는 email을 가진 member가 있는지 조회 - 존재하지 않음")
    @Transactional
    void isExistEmail_success_NotExist() throws Exception {
        mockMvc.perform(get("/api/v1/members/check?email=email@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(("false")))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("해당하는 email을 가진 member가 있는지 조회 - email validation 실패")
    @Transactional
    void isExistEmail_fail_nickname_validation() throws Exception {
        mockMvc.perform(get("/api/v1/members/check?email=e"))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }
}