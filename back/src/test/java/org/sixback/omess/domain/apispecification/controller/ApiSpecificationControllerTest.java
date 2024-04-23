package org.sixback.omess.domain.apispecification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiSpecificationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiSpecificationController apiSpecificationController;

    @Autowired
    ApiSpecificationService apiSpecificationService;

    @Autowired
    ApiSpecificationRepository apiSpecificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ObjectMapper objectMapper;

    private MockHttpSession mockitoSession;

    @BeforeEach
    public void setUp(){
        mockitoSession = new MockHttpSession();
    }

    @AfterEach
    public void cleanUpRepository() {
        apiSpecificationRepository.deleteAll();
        projectMemberRepository.deleteAll();
        projectRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("API 명세서 생성 성공 통합 테스트")
        //FIXME : 추후 Project에 속한 Member인지 확인하는 AOP or Filter로 대체
    void newTest() throws Exception {
        //given
        CreateApiSpecificationRequest request = new CreateApiSpecificationRequest(
                "testName", "testCategory"
        );

        Member member = TestUtils.makeMember();
        Member savedMember = memberRepository.save(member);
        mockitoSession.setAttribute("memberId", savedMember.getId());

        Project project = TestUtils.makeProject();
        Project savedProject = projectRepository.save(project);

        projectMemberRepository.save(new ProjectMember(savedProject, savedMember));

        //when

        //then
        mockMvc.perform(
                post("/api/v1/projects/{projectId}/api-specifications", savedProject.getId())
                        .session(mockitoSession)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print());
    }
}