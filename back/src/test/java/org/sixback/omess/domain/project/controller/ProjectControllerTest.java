package org.sixback.omess.domain.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.UpdateProjectRequest;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.sixback.omess.common.TestUtils.*;
import static org.sixback.omess.common.exception.ErrorType.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("create Project test ")
    class CreateProject {
        @Test
        @DisplayName("프로젝트 생성 - 성공")
        void createProject_success() throws Exception {
            // given
            String createProjectRequest = objectMapper.writeValueAsString(new CreateProjectRequest("pName"));

            Member member = makeMember();
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .content(createProjectRequest)
                            .session(session))
                    // then
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.projectId").isNumber())
                    .andExpect(jsonPath("$.name").value("pName"))
                    .andExpect(jsonPath("$.projectRole").value("OWNER"))
            ;
        }

        @Test
        @DisplayName("프로젝트 생성 - 로그인 안된 사용자 실패")
        void createProject_fail_notSignIn() throws Exception {
            // given
            String createProjectRequest = objectMapper.writeValueAsString(new CreateProjectRequest("pName"));

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .content(createProjectRequest))
                    // then
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.type").value(NEED_AUTHENTICATION_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
            ;
        }

        @Test
        @DisplayName("프로젝트 생성 - 빈 바디 실패")
        void createProject_fail_noBody() throws Exception {
            // given
            Member member = makeMember();
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value(INCOMPLETE_REQUEST_BODY_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
            ;
        }

        @DisplayName("프로젝트 생성 - 값 검증으로 인한 실패")
        @ValueSource(strings = {"", "1234567890_"})
        @ParameterizedTest
        void createProject_fail_validation(String name) throws Exception {
            // given
            String createProjectRequest = objectMapper.writeValueAsString(new CreateProjectRequest(name));

            Member member = makeMember();
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .content(createProjectRequest)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
            ;
        }
    }


    @Nested
    class UpdateProject {
        @Test
        @DisplayName("프로젝트 생성 - 성공")
        void updateProject_success() throws Exception {
            // given
            Member member = makeMember();
            Project project = makeProject("name");
            memberRepository.save(member);
            projectRepository.save(project);
            MockHttpSession session = makeMockSession(member.getId());

            String updateProjectRequest = objectMapper.writeValueAsString(new UpdateProjectRequest("pName"));

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest)
                            .session(session))
                    // then
                    .andExpect(status().isOk())
            ;

            Assertions.assertThat(project.getName()).isEqualTo("pName");
        }

        // TODO
        @Test
        @DisplayName("프로젝트 생성 - 로그인 안된 사용자 실패")
        void updateProject_fail_notSignIn() throws Exception {
            // given
            Member member = makeMember();
            Project project = makeProject("name");
            memberRepository.save(member);
            projectRepository.save(project);

            String updateProjectRequest = objectMapper.writeValueAsString(new UpdateProjectRequest("pName"));

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest))
                    // then
                    .andExpect(jsonPath("$.type").value(NEED_AUTHENTICATION_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
            ;
        }

        @Test
        @DisplayName("프로젝트 생성 - 빈 바디 실패")
        void updateProject_fail_noBody() throws Exception {
            // given
            Member member = makeMember();
            Project project = makeProject("name");
            memberRepository.save(member);
            projectRepository.save(project);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value(INCOMPLETE_REQUEST_BODY_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
            ;
        }

        @CsvSource(value = {" :USER", "pName: "}, delimiter = ':')
        @ParameterizedTest
        @DisplayName("프로젝트 생성 - 값 검증으로 인한 실패")
        void updateProject_fail_validation(String name, String projectRole) throws Exception {
            // given
            Member member = makeMember();
            Project project = makeProject("name");
            memberRepository.save(member);
            projectRepository.save(project);
            MockHttpSession session = makeMockSession(member.getId());

            String updateProjectRequest = objectMapper.writeValueAsString(new UpdateProjectRequest(""));

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                    .andExpect(jsonPath("$.title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
            ;
        }
    }
}