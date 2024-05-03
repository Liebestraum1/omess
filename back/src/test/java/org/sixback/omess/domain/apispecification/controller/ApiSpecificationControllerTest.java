package org.sixback.omess.domain.apispecification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.sixback.omess.domain.apispecification.model.dto.request.*;
import org.sixback.omess.domain.apispecification.model.entity.Api;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.sixback.omess.domain.apispecification.repository.*;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.sixback.omess.domain.member.model.dto.request.SignInMemberRequest;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sixback.omess.common.TestUtils.makeMember;
import static org.sixback.omess.common.TestUtils.makeProject;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationMapper.*;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils.generatePath;
import static org.sixback.omess.domain.project.model.enums.ProjectRole.USER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    MemberRepository memberRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private RequestHeaderRepository requestHeaderRepository;

    @Autowired
    private QueryParamRepository queryParamRepository;

    @Autowired
    private PathVariableRepository pathVariableRepository;

    @Autowired
    ObjectMapper objectMapper;

    private MockHttpSession mockitoSession;
    private Project dummyProjectForSetUp;
    private Member dummyMemberForSetUp;
    private ApiSpecification dummyApiSpecificationForSetUp;
    private Domain dummyDomainForSetUp;
    private Api dummyApiForSetUp;
    private Cookie cookie;

    @BeforeEach
    public void setUp() {
        dummyProjectForSetUp = makeProject();
        dummyMemberForSetUp = makeMember("nickname", "e@naver.com", "password123");
        memberRepository.save(dummyMemberForSetUp);
        projectRepository.save(dummyProjectForSetUp);
        projectMemberRepository.save(new ProjectMember(dummyProjectForSetUp, dummyMemberForSetUp, USER));
//        mockitoSession = makeMockSession(dummyMemberForSetUp.getId());

        try {
            MockHttpServletResponse response = mockMvc.perform(post("/api/v1/members/signin")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new SignInMemberRequest("e@naver.com", "password123"))))
                    .andReturn()
                    .getResponse();
            cookie = response.getCookie("SESSION");
        } catch (Exception e) {
            System.out.println("login 실패");
            throw new RuntimeException("");
        }
    }

    @AfterEach
    public void cleanUpRepository() {
        apiRepository.deleteAll();
        domainRepository.deleteAll();
        apiSpecificationRepository.deleteAll();
        projectRepository.deleteAll();
        memberRepository.deleteAll();
    }

    private void setUpDummyApiSpecification(Project project) {
        dummyApiSpecificationForSetUp = ApiSpecification.builder()
                .moduleName("testModule")
                .moduleCategory("testCategory")
                .project(project)
                .build();

        apiSpecificationRepository.save(dummyApiSpecificationForSetUp);
        dummyApiSpecificationForSetUp.addPath("P" + project.getId() + "/A" + dummyApiSpecificationForSetUp.getId());
        apiSpecificationRepository.save(dummyApiSpecificationForSetUp);
    }

    private void setUpDummyDomain(ApiSpecification apiSpecification) {
        dummyDomainForSetUp = new Domain("testDomain", apiSpecification);
        domainRepository.save(dummyDomainForSetUp);
        dummyDomainForSetUp.addPath(
                "P" + dummyProjectForSetUp.getId() + "/A" + dummyApiSpecificationForSetUp.getId() + "/D"
                        + dummyDomainForSetUp.getId());
        domainRepository.save(dummyDomainForSetUp);
    }

    private void setUpDummyApi(Domain domain) {
        dummyApiForSetUp = Api.builder()
                .domain(domain)
                .method("GET")
                .name("testName1")
                .description("This is a test description for GET")
                .endpoint("/api/test1")
                .statusCode((short) 200)
                .requestSchema("{ \"testKey\": \"testValue\" }")
                .responseSchema("{ \"testKey\": \"testValue\" }")
                .build();

        apiRepository.save(dummyApiForSetUp);
        dummyApiForSetUp.addPath(
                "P" + dummyProjectForSetUp.getId() + "/A" + dummyApiSpecificationForSetUp.getId() + "/D" + domain.getId()
                        + "/A" + dummyApiForSetUp.getId());

        createDummyPathVariables().forEach(
                pathVariable -> dummyApiForSetUp.getPathVariables().add(toPathVariable(pathVariable,
                        dummyApiForSetUp)));
        createDummyQueryParams().forEach(queryParam -> dummyApiForSetUp.getQueryParams().add(toQueryParam(queryParam,
                dummyApiForSetUp)));
        createDummyRequestHeaders().forEach(
                requestHeader -> dummyApiForSetUp.getRequestHeaders().add(toRequestHeader(requestHeader,
                        dummyApiForSetUp)));

        apiRepository.save(dummyApiForSetUp);
    }

    @DisplayName("API 명세서 조회 테스트")
    @Test
    void getSetUpDummyApiSpecification() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);
        setUpDummyApi(dummyDomainForSetUp);

        //when

        //then
        mockMvc.perform(
                        get("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}", dummyProjectForSetUp.getId(),
                                dummyApiSpecificationForSetUp.getId())
//                                .session(mockitoSession)
                                .cookie(cookie)
                ).andExpect(status().isOk())
//			.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.domains").isArray())
                .andExpect(jsonPath("$.domains").isNotEmpty())
                .andExpect(jsonPath("$.domains[0].apis").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("API 명세서 생성 성공 테스트")
    void createSetUpDummyApiSpecificationSuccessTest() throws Exception {
        //given
        CreateApiSpecificationRequest request = new CreateApiSpecificationRequest("testName", "testCategory");

        //when
        mockMvc.perform(
                        post("/api/v1/projects/{projectId}/api-specifications", dummyProjectForSetUp.getId())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
//                                .session(mockitoSession)
                                .cookie(cookie)
                ).andExpect(status().isOk())
                .andDo(print());

        List<ApiSpecification> apiSpecifications = apiSpecificationRepository.findAll();

        //then
        String uri = String.format("/api/v1/projects/%d/api-specifications", dummyProjectForSetUp.getId());
        String path = generatePath(uri, apiSpecifications.getFirst().getId());

        assertThat(apiSpecifications.size()).isEqualTo(1);
        assertThat(apiSpecifications.getFirst().getPath()).isEqualTo(path);
        assertThat(apiSpecifications.getFirst().getTitle()).isEqualTo(request.name());
        assertThat(apiSpecifications.getFirst().getCategory()).isEqualTo(request.category());

    }

    @Test
    @DisplayName("도메인 생성 성공 테스트")
    void createSetUpDummyDomainSuccessTest() throws Exception {
        //given
        CreateDomainRequest request = new CreateDomainRequest("testName");
        setUpDummyApiSpecification(dummyProjectForSetUp);

        //when
        mockMvc.perform(post("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId()
                        ).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        List<Domain> domains = domainRepository.findAll();

        //then
        assertThat(domains.size()).isEqualTo(1);

        String uri = String.format("/api/v1/projects/%d/api-specifications/%d/domains", dummyProjectForSetUp.getId(),
                dummyApiSpecificationForSetUp.getId());
        String path = generatePath(uri, domains.getFirst().getId());

        assertThat(domains.getFirst().getPath()).isEqualTo(path);
        assertThat((domains.getFirst().getName())).isEqualTo(request.name());
    }

    @DisplayName("단일 API 생성 성공 테스트")
    @ParameterizedTest
    @MethodSource("provideCreateApiRequest")
    void createApiSuccessTest(CreateApiRequest request) throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);

        //when
        mockMvc.perform(
                        post("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}/apis",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId()
                        ).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(apiSpecificationRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("도메인 목록 조회 테스트")
    void getDomainsTest() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);

        Domain dummyDomain2 = new Domain("dummyDomain2", dummyApiSpecificationForSetUp);
        Domain dummyDomain3 = new Domain("dummyDomain3", dummyApiSpecificationForSetUp);
        Domain dummyDomain4 = new Domain("dummyDomain4", dummyApiSpecificationForSetUp);

        domainRepository.save(dummyDomain2);
        domainRepository.save(dummyDomain3);
        domainRepository.save(dummyDomain4);

        //when
        mockMvc.perform(
                        get("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId())
                                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domains").isArray())
                .andExpect(jsonPath("$.domains").isNotEmpty())
                .andExpect(jsonPath("$.domains[0].name").value(dummyDomainForSetUp.getName()))
                .andExpect(jsonPath("$.domains[1].name").value(dummyDomain2.getName()))
                .andExpect(jsonPath("$.domains[2].name").value(dummyDomain3.getName()))
                .andExpect(jsonPath("$.domains[3].name").value(dummyDomain4.getName()))
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("도메인 수정 성공 테스트")
    void updateDomainTest() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);

        UpdateDomainRequest request = new UpdateDomainRequest("updatedName");

        //when
        mockMvc.perform(
                        patch("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
//                                .session(mockitoSession)
                                .cookie(cookie)
                ).andExpect(status().isOk())
                .andDo(print());

        //then
        String path = "P" + dummyProjectForSetUp.getId() + "/A" + dummyApiSpecificationForSetUp.getId() + "/D"
                + dummyDomainForSetUp.getId();
        Optional<Domain> updatedDomain = domainRepository.findByPath(path);
        assertThat(updatedDomain).isPresent();
        assertThat(updatedDomain.get().getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("도메인 삭제 성공 테스트")
    void deleteDomainSuccessTest() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);
        setUpDummyApi(dummyDomainForSetUp);

        //when
        mockMvc.perform(
                        delete("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId())
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(apiRepository.findAll()).hasSize(0);
        assertThat(domainRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("API 조회 테스트")
    void getApiTest() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);
        setUpDummyApi(dummyDomainForSetUp);

        //when

        //then
        mockMvc.perform(
                        get("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}/apis/{apiId}",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId(),
                                dummyApiForSetUp.getId())
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiId").value(dummyApiForSetUp.getId()))
                .andExpect(jsonPath("$.method").value(dummyApiForSetUp.getMethod()))
                .andExpect(jsonPath("$.name").value(dummyApiForSetUp.getName()))
                .andExpect(jsonPath("$.description").value(dummyApiForSetUp.getDescription()))
                .andExpect(jsonPath("$.endpoint").value(dummyApiForSetUp.getEndpoint()))
                .andExpect(jsonPath("$.requestSchema").value(dummyApiForSetUp.getRequestSchema()))
                .andExpect(jsonPath("$.responseSchema").value(dummyApiForSetUp.getResponseSchema()))
                .andExpect(jsonPath("$.endpoint").value(dummyApiForSetUp.getEndpoint()))
                .andExpect(jsonPath("$.requestHeaders").isArray())
                .andExpect(jsonPath("$.requestHeaders").isNotEmpty())
                .andExpect(jsonPath("$.queryParams").isArray())
                .andExpect(jsonPath("$.queryParams").isNotEmpty())
                .andExpect(jsonPath("$.pathVariables").isArray())
                .andExpect(jsonPath("$.pathVariables").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("API 수정 성공 테스트")
    @ParameterizedTest
    @MethodSource("provideUpdateApiRequest")
    void updateApiSuccessTest(UpdateApiRequest request) throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);
        setUpDummyApi(dummyDomainForSetUp);

        //when
        mockMvc.perform(
                        put("/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}/apis/{apiId}",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId(),
                                dummyApiForSetUp.getId()
                        )
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Optional<Api> optionalApi = apiRepository.findByPath(dummyApiForSetUp.getPath());

        //then
        assertThat(optionalApi).isPresent();
        Api api = optionalApi.get();
        assertThat(api.getMethod()).isEqualTo(request.getMethod());
        assertThat(api.getName()).isEqualTo(request.getName());
        assertThat(api.getDescription()).isEqualTo(request.getDescription());
        assertThat(api.getStatusCode()).isEqualTo(request.getStatusCode());
        assertThat(api.getEndpoint()).isEqualTo(request.getEndpoint());
        assertThat(api.getRequestSchema()).isEqualTo(request.getRequestSchema());
        assertThat(api.getResponseSchema()).isEqualTo(request.getResponseSchema());

        assertThat(requestHeaderRepository.findAll().size())
                .isEqualTo(request.getUpdateRequestHeaderRequests() == null ? 0 : request.getUpdateRequestHeaderRequests().size());
        assertThat(queryParamRepository.findAll().size())
                .isEqualTo(request.getUpdateQueryParamRequests() == null ? 0 : request.getUpdateQueryParamRequests().size());
        assertThat(pathVariableRepository.findAll().size())
                .isEqualTo(request.getUpdatePathVariableRequests() == null ? 0 : request.getUpdatePathVariableRequests().size());
    }

    @Test
    @DisplayName("API 삭제 성공 테스트")
    void deleteApiSuccessTest() throws Exception {
        //given
        setUpDummyApiSpecification(dummyProjectForSetUp);
        setUpDummyDomain(dummyApiSpecificationForSetUp);
        setUpDummyApi(dummyDomainForSetUp);

        //when
        mockMvc.perform(
                        delete(
                                "/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}/apis/{apiId}",
                                dummyProjectForSetUp.getId(), dummyApiSpecificationForSetUp.getId(), dummyDomainForSetUp.getId(),
                                dummyApiForSetUp.getId())
//                                .session(mockitoSession)
                                .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(domainRepository.findAll()).hasSize(1);
        assertThat(apiRepository.findAll()).hasSize(0);
        assertThat(pathVariableRepository.findAll()).hasSize(0);
        assertThat(queryParamRepository.findAll()).hasSize(0);
        assertThat(requestHeaderRepository.findAll()).hasSize(0);
    }

    private static Stream<CreateApiRequest> provideCreateApiRequest() {
        return Stream.of(
                CreateApiRequest.builder()
                        .method("GET")
                        .name("testName1")
                        .description("This is a test description for GET")
                        .endpoint("/api/test1")
                        .statusCode((short) 200)
                        .requestSchema("{ \"testKey\": \"testValue\" }")
                        .responseSchema("{ \"testKey\": \"testValue\" }")
                        .createRequestHeaderRequests(createDummyRequestHeaders())
                        .createQueryParamRequests(createDummyQueryParams())
                        .createPathVariableRequests(createDummyPathVariables())
                        .build(),
                CreateApiRequest.builder()
                        .method("POST")
                        .name("testName2")
                        .endpoint("/api/test2")
                        .statusCode((short) 404)
                        .build(),
                CreateApiRequest.builder()
                        .method("PUT")
                        .name("testName3")
                        .endpoint("/api/test3")
                        .statusCode((short) 200)
                        .requestSchema("{ \"testKey\": \"testValue\" }")
                        .createRequestHeaderRequests(createDummyRequestHeaders())
                        .createPathVariableRequests(createDummyPathVariables())
                        .build()
        );
    }

    private static List<CreateRequestHeaderRequest> createDummyRequestHeaders() {
        return Arrays.asList(
                new CreateRequestHeaderRequest("Content-Type", "application/json"),
                new CreateRequestHeaderRequest("Accept", "application/json")
        );
    }

    private static List<CreateQueryParamRequest> createDummyQueryParams() {
        return Arrays.asList(
                new CreateQueryParamRequest("page", "페이지 숫자입니다."),
                new CreateQueryParamRequest("limit")
        );
    }

    private static List<CreatePathVariableRequest> createDummyPathVariables() {
        return Arrays.asList(
                new CreatePathVariableRequest("userId", "This is a user ID"),
                new CreatePathVariableRequest("testId")
        );
    }

    private static Stream<UpdateApiRequest> provideUpdateApiRequest() {
        return Stream.of(
                UpdateApiRequest.builder()
                        .method("POST")
                        .name("updateTestName1")
                        .description("This is a update test description for POST")
                        .endpoint("/api/update/test1")
                        .statusCode((short) 200)
                        .requestSchema("{ \"updateTestKey\": \"updateTestValue\" }")
                        .responseSchema("{ \"updateTestKey\": \"updateTestValue\" }")
                        .updateRequestHeaderRequests(updateDummyRequestHeaders())
                        .updateQueryParamRequests(updateDummyQueryParams())
                        .updatePathVariableRequests(updateDummyPathVariables())
                        .build(),
                UpdateApiRequest.builder()
                        .method("GET")
                        .name("updateTstName2")
                        .endpoint("/api/update/test2")
                        .statusCode((short) 404)
                        .build(),
                UpdateApiRequest.builder()
                        .method("PATCH")
                        .name("updateTestName3")
                        .endpoint("/api/update/test3")
                        .statusCode((short) 200)
                        .requestSchema("{ \"updateTestKey\": \"updateTestValue\" }")
                        .updateRequestHeaderRequests(updateDummyRequestHeaders())
                        .updatePathVariableRequests(updateDummyPathVariables())
                        .build()
        );
    }

    private static List<UpdateRequestHeaderRequest> updateDummyRequestHeaders() {
        return Arrays.asList(
                new UpdateRequestHeaderRequest("Authorization", "application/json"),
                new UpdateRequestHeaderRequest("Content-Type", "application/json")
        );
    }

    private static List<UpdateQueryParamRequest> updateDummyQueryParams() {
        return Arrays.asList(
                new UpdateQueryParamRequest("limit", "페이지 숫자입니다."),
                new UpdateQueryParamRequest("page")
        );
    }

    private static List<UpdatePathVariableRequest> updateDummyPathVariables() {
        return Arrays.asList(
                new UpdatePathVariableRequest("updatedId", "This is a updated ID"),
                new UpdatePathVariableRequest("testId")
        );
    }
}