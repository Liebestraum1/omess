package org.sixback.omess.domain.apispecification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateDomainRequest;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.repository.DomainRepository;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils.generatePath;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(apiSpecificationController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();
        mockitoSession = new MockHttpSession();
    }

    @AfterEach
    public void cleanUpRepository() {
        domainRepository.deleteAll();
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
        String path = generatePath(contextPath, 1L);
        //when

        //then
        assertThat(path).isEqualTo("P1/A1");
    }

    @Test
    @DisplayName("API 명세서 생성 성공 테스트")
    void createApiSpecificationSuccessTest() throws Exception {
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

        List<ApiSpecification> apiSpecifications = apiSpecificationRepository.findAll();

        //then
        String uri = String.format("/api/v1/projects/%d/api-specifications", project.getId());
        String path = generatePath(uri, apiSpecifications.getFirst().getId());

        assertThat(apiSpecifications.size()).isEqualTo(1);
        assertThat(apiSpecifications.getFirst().getPath()).isEqualTo(path);
        assertThat(apiSpecifications.getFirst().getTitle()).isEqualTo(request.name());
        assertThat(apiSpecifications.getFirst().getCategory()).isEqualTo(request.category());

    }

    @Test
    @DisplayName("도메인 생성 성공 테스트")
    void createDomainSuccessTest() throws Exception {
        //given
        CreateDomainRequest request = new CreateDomainRequest("testName");
        Project project = TestUtils.makeProject();
        ApiSpecification apiSpecification = apiSpecification(project);

        projectRepository.save(project);
        apiSpecificationRepository.save(apiSpecification);
        apiSpecification.addPath("P"+project.getId()+"/A"+apiSpecification.getId());
        apiSpecificationRepository.save(apiSpecification);


        //when
        mockMvc.perform(
                post("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains", project.getId(), apiSpecification.getId()
                ).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        List<Domain> domains = domainRepository.findAll();

        //then
        assertThat(domains.size()).isEqualTo(1);

        String uri = String.format("/api/v1/projects/%d/api-specifications/%d/domains", project.getId(), apiSpecification.getId());
        String path = generatePath(uri, domains.getFirst().getId());

        assertThat(domains.getFirst().getPath()).isEqualTo(path);
        assertThat((domains.getFirst().getName())).isEqualTo(request.name());
    }
}