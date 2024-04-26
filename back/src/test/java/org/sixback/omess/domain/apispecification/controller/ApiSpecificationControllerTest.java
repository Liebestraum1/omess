package org.sixback.omess.domain.apispecification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.repository.DomainRepository;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    ProjectRepository projectRepository;

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
        projectRepository.deleteAll();
    }

    private ApiSpecification apiSpecification(Project project) {
        return ApiSpecification.builder()
                .moduleName("testModule")
                .moduleCategory("testCategory")
                .project(project)
                .build();
    }

    @Test
    @DisplayName("")
    void newTest(){
        //given
        String contextPath = "/api/v1/projects/1/api-specifications";
        String path = ApiSpecificationUtils.generatePath(contextPath, 1L);
        //when

        //then
        assertThat(path).isEqualTo("P1/A1");
    }

    @Test
    @DisplayName("API 명세서 생성 성공 테스트")
    void apiSpecificationSuccessTest() throws Exception {
        //given
        CreateApiSpecificationRequest request = new CreateApiSpecificationRequest("testName", "testCategory");
        Project project = TestUtils.makeProject();
        projectRepository.save(project);

        //when
        mockMvc.perform(
                    post("/api/v1/projects/{projectId}/api-specifications", project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andDo(print());

        Optional<ApiSpecification> optionalApiSpecification = apiSpecificationRepository.findByProjectId(project.getId());

        //then
        assertThat(optionalApiSpecification.isPresent()).isTrue();

        ApiSpecification apiSpecification = optionalApiSpecification.get();
        assertThat(apiSpecification.getPath()).isEqualTo("P" + project.getId() + "/A" + apiSpecification.getId());
        assertThat(apiSpecification.getTitle()).isEqualTo(request.name());
        assertThat(apiSpecification.getCategory()).isEqualTo(request.category());

    }

}