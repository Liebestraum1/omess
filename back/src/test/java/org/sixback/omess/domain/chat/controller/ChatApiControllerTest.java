package org.sixback.omess.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.common.RandomUtils;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.domain.chat.model.dto.request.ChatCreateRequest;
import org.sixback.omess.domain.chat.model.dto.response.ChatInfo;
import org.sixback.omess.domain.chat.repository.ChatRepository;
import org.sixback.omess.domain.chat.service.ChatMemberService;
import org.sixback.omess.domain.chat.service.ChatService;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.member.service.MemberService;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ChatApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    ChatMemberService chatMemberService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired // 의존성을 자동으로 주입합니다.
    ChatService chatService;


    ObjectMapper mapper = new ObjectMapper();

    Member member1;
    Member member2;
    Member member3;
    Project project;

    @BeforeEach
    void setup() {
        member1 = TestUtils.makeMember();
        member2 = TestUtils.makeMember();
        member3 = TestUtils.makeMember();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        project = TestUtils.makeProject();
        projectRepository.save(project);
    }

    @AfterEach
    void after() {
        chatRepository.deleteAll().block();
    }


    @Test
    void create_chat_test() throws Exception {
        String endpointUrl = "/api/v1/chat/" + project.getId();
        List<String> emails = List.of(member2.getEmail(), member3.getEmail());
        ChatCreateRequest request = new ChatCreateRequest(emails, "title");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", member1.getId());
        // MockMvc를 사용하여 POST 요청 수행 및 응답 검증
        MvcResult mvcResult = mockMvc.perform(post(endpointUrl)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        ChatInfo chatInfo = mapper.readValue(contentAsString, ChatInfo.class);
        assertThat(chatInfo.getName()).isEqualTo("title");
    }


    @Test
    void load_my_chat_list() throws Exception {
        String endpointUrl = "/api/v1/chat/" + project.getId();
        List<String> emails = List.of(member2.getEmail(), member3.getEmail());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", member1.getId());

        int random = RandomUtils.generateRandomNumber(10);

        for (int i = 0; i < random; i++) {
            chatService.createChat(project.getId(), member1.getId(), RandomUtils.generateRandomString(4), emails)
                    .block();
        }

        MvcResult mvcResult = mockMvc.perform(get(endpointUrl)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(random))
                .andDo(print());

    }


    @Test
    void leave_chat() throws Exception {
        List<String> emails = List.of(member2.getEmail(), member3.getEmail());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", member2.getId());

        int random = RandomUtils.generateRandomNumber(10);

        List<String> myChatIdList = new ArrayList<>();
        for (int i = 0; i < random; i++) {
            ChatInfo chatInfo = chatService.createChat(project.getId(), member1.getId(), RandomUtils.generateRandomString(4), emails)
                    .block();
            myChatIdList.add(chatInfo.getId());
        }

        int randomIdx = RandomUtils.generateRandomNumber(myChatIdList.size());
        String endpointUrl = "/api/v1/chat/" + project.getId() + "/" + myChatIdList.get(randomIdx);
        MvcResult mvcResult = mockMvc.perform(delete(endpointUrl)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(print());
    }

}