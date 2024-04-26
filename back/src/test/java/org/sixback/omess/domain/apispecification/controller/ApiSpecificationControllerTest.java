package org.sixback.omess.domain.apispecification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.repository.DomainRepository;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
    DomainRepository domainRepository;

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

    private ApiSpecification apiSpecification(Project project) {
        return ApiSpecification.builder()
                .moduleName("testModule")
                .moduleCategory("testCategory")
                .project(project)
                .build();
    }

}