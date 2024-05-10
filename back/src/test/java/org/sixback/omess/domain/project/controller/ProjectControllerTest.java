package org.sixback.omess.domain.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sixback.omess.common.ProjectMemberResult;
import org.sixback.omess.domain.member.model.dto.request.SignInMemberRequest;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.InviteProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.UpdateProjectRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.sixback.omess.common.TestUtils.*;
import static org.sixback.omess.common.exception.ErrorType.*;
import static org.sixback.omess.domain.project.model.enums.ProjectRole.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    String email = "email@naver.com";
    String nickname = "nickname";
    String password = "password123";

    @Nested
    @DisplayName("create Project test ")
    class CreateProject {
        @Test
        @DisplayName("프로젝트 생성 - 성공")
        void createProject_success() throws Exception {
            // given
            String createProjectRequest = objectMapper.writeValueAsString(new CreateProjectRequest("pName"));

            Member member = makeMember(nickname, email, password);
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .content(createProjectRequest)
//                            .session(session))
                            .cookie(cookie))
                    // then
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.projectId").isNumber())
                    .andExpect(jsonPath("$.name").value("pName"))
                    .andExpect(jsonPath("$.projectRole").value("OWNER"))
                    .andDo(print())
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
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 생성 - 빈 바디 실패")
        void createProject_fail_noRequestBody() throws Exception {
            // given
            Member member = makeMember(nickname, email, password);
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
                    .andDo(print())
            ;
        }

        @DisplayName("프로젝트 생성 - 값 검증으로 인한 실패")
        @ValueSource(strings = {"", "1234567890_"})
        @ParameterizedTest
        void createProject_fail_validation(String name) throws Exception {
            // given
            String createProjectRequest = objectMapper.writeValueAsString(new CreateProjectRequest(name));

            Member member = makeMember(nickname, email, password);
            memberRepository.save(member);
            MockHttpSession session = makeMockSession(member.getId());

            // when
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(APPLICATION_JSON)
                            .content(createProjectRequest)
                            .session(session))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("유효한 요청이 아닙니다."))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("get Projects test")
    class GetProjects {
        @Test
        @DisplayName("프로젝트 조회 - 성공")
        void getProjects_success() throws Exception {
            // given
            ProjectMemberResult projectMemberResult = makeProjectMembers(nickname, email, password);
            memberRepository.save(projectMemberResult.getMember());
            projectRepository.saveAll(projectMemberResult.getProjects());
            projectMemberRepository.saveAll(projectMemberResult.getProjectMembers());
            MockHttpSession session = makeMockSession(projectMemberResult.getMember().getId());
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(get("/api/v1/projects")
//                            .session(session)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.projects").isArray())
                    .andExpect(jsonPath("$.projects[0].projectId").value(projectMemberResult.getProjects().get(0).getId()))
                    .andExpect(jsonPath("$.projects[0].name").value(projectMemberResult.getProjects().get(0).getName()))
                    .andExpect(jsonPath("$.projects[1].projectId").value(projectMemberResult.getProjects().get(1).getId()))
                    .andExpect(jsonPath("$.projects[1].name").value(projectMemberResult.getProjects().get(1).getName()))
                    .andExpect(jsonPath("$.projects[2].projectId").value(projectMemberResult.getProjects().get(2).getId()))
                    .andExpect(jsonPath("$.projects[2].name").value(projectMemberResult.getProjects().get(2).getName()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 조회 - 로그인 안된 사용자 실패")
        void getProjects_fail_notSignin() throws Exception {
            // given
            ProjectMemberResult projectMemberResult = makeProjectMembers();
            memberRepository.save(projectMemberResult.getMember());
            projectRepository.saveAll(projectMemberResult.getProjects());
            projectMemberRepository.saveAll(projectMemberResult.getProjectMembers());

            // when
            mockMvc.perform(get("/api/v1/projects"))
                    // then
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects"))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("get Projects test")
    class UpdateProject {
        @Test
        @DisplayName("프로젝트 업데이트 - 성공")
        void updateProject_success() throws Exception {
            // given
            ProjectMemberResult projectMemberResult = makeProjectMembers(nickname, email, password);
            Member member = projectMemberResult.getMember();
            Project project = projectMemberResult.getProjects().getFirst();
            memberRepository.save(member);
            projectRepository.saveAll(projectMemberResult.getProjects());
            projectMemberRepository.save(projectMemberResult.getProjectMembers().getFirst());
            MockHttpSession session = makeMockSession(member.getId());
            Cookie cookie = getCookie(email, password);

            String updateProjectRequest = objectMapper.writeValueAsString(new UpdateProjectRequest("pName"));

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest)
                            .session(session)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isOk())
                    .andDo(print())
            ;

            Assertions.assertThat(project.getName()).isEqualTo("pName");
        }

        @Test
        @DisplayName("프로젝트 업데이트 - 로그인 안된 사용자 실패")
        void updateProject_unAuthorized_fail() throws Exception {
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
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 업데이트 - 권한 없음 실패")
        void updateProject_permission_fail() throws Exception {
            // given
            Member member = makeMember();
            Member member2 = makeMember("nickn", "email1@naver.com", "password1234");
            Project project = makeProject("name");

            memberRepository.save(member);
            memberRepository.save(member2);
            projectRepository.save(project);

            Cookie cookie = getCookie("email1@naver.com", "password1234");

            String updateProjectRequest = objectMapper.writeValueAsString(new UpdateProjectRequest("pName"));

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.title").value(UNAUTHORIZED_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 업데이트 - 빈 바디 실패")
        void updateProject_fail_noBody() throws Exception {
            // given
            ProjectMemberResult projectMemberResult = makeProjectMembers(nickname, email, password);
            Member member = projectMemberResult.getMember();
            Project project = projectMemberResult.getProjects().getFirst();
            memberRepository.save(member);
            projectRepository.saveAll(projectMemberResult.getProjects());
            projectMemberRepository.save(projectMemberResult.getProjectMembers().getFirst());
            MockHttpSession session = makeMockSession(member.getId());
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .session(session)
                            .cookie(cookie))

                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @ValueSource(strings = {" ", "01234567890_"})
        @ParameterizedTest
        @DisplayName("프로젝트 업데이트 - 값 검증으로 인한 실패")
        void updateProject_fail_validation(String name) throws Exception {
            // given
            Member member = makeMember(nickname, email, password);
            Project project = makeProject("name");
            memberRepository.save(member);
            projectRepository.save(project);
            projectMemberRepository.save(new ProjectMember(project, member, USER));
            MockHttpSession session = makeMockSession(member.getId());
            Cookie cookie = getCookie(email, password);

            String updateProjectRequest = "{ "
                    + "\"name\":" + "\"" + name + "\""
                    + "}";

            // when
            mockMvc.perform(patch("/api/v1/projects/" + project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(updateProjectRequest)
                            .session(session)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }
    }

    // 프로젝트 권한
    // 로그인 권한
    // 밸리테이션 체크
    @Nested
    class InviteProject {
        @Test
        @DisplayName("프로젝트 초대 - 성공")
        void inviteProject_success() throws Exception {
            // given
            Project project = makeProject();
            Member member = makeMember("nickname", email, password);
            ProjectMember projectMember = makeProjectMember(project, member);
            projectRepository.save(project);
            memberRepository.save(member);
            projectMemberRepository.save(projectMember);

            Cookie cookie = getCookie(email, password);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            List<Long> invitedMember = members.stream()
                    .map(Member::getId)
                    .toList();
            InviteProjectRequest inviteProjectRequest = new InviteProjectRequest(invitedMember);
            String input = objectMapper.writeValueAsString(inviteProjectRequest);

            // when
            mockMvc.perform(post("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(input)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isCreated())
                    .andDo(print())
            ;

            List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject_Id(project.getId());
            List<Long> result = projectMembers
                    .stream()
                    .map(pm -> pm.getMember().getId())
                    .toList();
            Assertions.assertThat(result).containsAll(invitedMember);
        }

        @Test
        @DisplayName("프로젝트 초대 - 인증 실패")
        void inviteProject_unAuthorized_fail() throws Exception {
            // given
            Project project = makeProject();
            Member member = makeMember("nickname", "email@naver.com", "password123");
            ProjectMember projectMember = makeProjectMember(project, member);
            projectRepository.save(project);
            memberRepository.save(member);
            projectMemberRepository.save(projectMember);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            List<Long> invitedMember = members.stream()
                    .map(Member::getId)
                    .toList();
            InviteProjectRequest inviteProjectRequest = new InviteProjectRequest(invitedMember);
            String input = objectMapper.writeValueAsString(inviteProjectRequest);

            // when
            mockMvc.perform(post("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(input))
                    // then
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.detail").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 초대 - 권한 실패")
        void inviteProject_permission_fail() throws Exception {
            // given
            Project project = makeProject();
            Member member = makeMember("nickname", "email@naver.com", "password123");
            projectRepository.save(project);
            memberRepository.save(member);
            Cookie cookie = getCookie(email, password);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            List<Long> invitedMember = members.stream()
                    .map(Member::getId)
                    .toList();
            InviteProjectRequest inviteProjectRequest = new InviteProjectRequest(invitedMember);
            String input = objectMapper.writeValueAsString(inviteProjectRequest);

            // when
            mockMvc.perform(post("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(input)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.title").value(UNAUTHORIZED_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 초대 - 빈 바디 실패")
        void inviteProject_noBody_fail() throws Exception {
            // given
            Project project = makeProject();
            Member member = makeMember("nickname", email, password);
            ProjectMember projectMember = makeProjectMember(project, member);
            projectRepository.save(project);
            memberRepository.save(member);
            projectMemberRepository.save(projectMember);

            Cookie cookie = getCookie(email, password);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            // when
            mockMvc.perform(post("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(INCOMPLETE_REQUEST_BODY_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트 초대 - 초대하고 하는 사용자의 리스트가 빈 요청 실패")
        void inviteProject_EmptyInvitedMembers_fail() throws Exception {
            // given
            Project project = makeProject();
            Member member = makeMember("nickname", email, password);
            ProjectMember projectMember = makeProjectMember(project, member);
            projectRepository.save(project);
            memberRepository.save(member);
            projectMemberRepository.save(projectMember);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            Cookie cookie = getCookie(email, password);
            InviteProjectRequest inviteProjectRequest = new InviteProjectRequest(new ArrayList<>());
            String input = objectMapper.writeValueAsString(inviteProjectRequest);

            // when
            mockMvc.perform(post("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .content(input)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(VALIDATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }
    }

    @Nested
    @DisplayName("프로젝트에 속한 멤버 조회")
    class getMemberInProjectTest {
        @Test
        @DisplayName("프로젝트에 속한 멤버 조회 - 성공")
        void getMemberInProjectTest_success() throws Exception {
            // given
            Member loginMember = makeMember("nickname", email, password);
            List<Member> members = makeMembers();
            members.add(loginMember);
            memberRepository.saveAll(members);

            Project project = makeProject();
            projectRepository.save(project);
            for (Member member : members) {
                projectMemberRepository.save(makeProjectMember(project, member));
            }
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(get("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(4))
                    .andExpect(jsonPath("$.[0].id").isNumber())
                    .andExpect(jsonPath("$.[0].nickname").isString())
                    .andExpect(jsonPath("$.[0].profile").isString())
                    .andExpect(jsonPath("$.[1].id").isNumber())
                    .andExpect(jsonPath("$.[1].nickname").isString())
                    .andExpect(jsonPath("$.[1].profile").isString())
                    .andExpect(jsonPath("$.[2].id").isNumber())
                    .andExpect(jsonPath("$.[2].nickname").isString())
                    .andExpect(jsonPath("$.[2].profile").isString())
                    .andExpect(jsonPath("$.[3].id").isNumber())
                    .andExpect(jsonPath("$.[3].nickname").isString())
                    .andExpect(jsonPath("$.[3].profile").isString())
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트에 속한 멤버 조회 - 인증 실패")
        void getMemberInProjectTest_noAuthorized_fail() throws Exception {
            // given
            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            Project project = makeProject();
            projectRepository.save(project);
            for (Member member : members) {
                projectMemberRepository.save(makeProjectMember(project, member));
            }
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(get("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON))
                    // then
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.title").value(NEED_AUTHENTICATION_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

        @Test
        @DisplayName("프로젝트에 속한 멤버 조회 - 권한 없음 실패")
        void getMemberInProjectTest_permission_fail() throws Exception {
            // given
            Member loginMember = makeMember("nickname", email, password);
            memberRepository.save(loginMember);

            List<Member> members = makeMembers();
            memberRepository.saveAll(members);

            Project project = makeProject();
            projectRepository.save(project);
            for (Member member : members) {
                projectMemberRepository.save(makeProjectMember(project, member));
            }
            Cookie cookie = getCookie(email, password);

            // when
            mockMvc.perform(get("/api/v1/projects/{projectId}", project.getId())
                            .contentType(APPLICATION_JSON)
                            .cookie(cookie))
                    // then
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.title").value(UNAUTHORIZED_ERROR.getTitle()))
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.instance").value("/api/v1/projects/" + project.getId()))
                    .andDo(print())
            ;
        }

    }


    private Cookie getCookie(String email, String password) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/members/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignInMemberRequest(email, password))))
                .andReturn()
                .getResponse();
        return response.getCookie("SESSION");
    }
}
