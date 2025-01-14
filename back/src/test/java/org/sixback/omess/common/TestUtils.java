package org.sixback.omess.common;

import org.sixback.omess.common.utils.PasswordUtils;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.model.enums.ProjectRole;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.sixback.omess.common.RandomUtils.*;
import static org.sixback.omess.domain.project.model.enums.ProjectRole.USER;

public class TestUtils {
    public static Member makeMember() {
        return new Member(generateRandomString(8), generateRandomEmail(), generateRandomString(20));
    }

    public static Member makeMember(String nickname, String email, String password) {
        String encodePassword = PasswordUtils.encodePassword(password);
        return new Member(nickname, email, encodePassword);
    }

    public static List<Member> makeMembers(int size) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            members.add(makeMember());
        }
        return members;
    }

    public static List<Member> makeMembers() {
        return makeMembers(3);
    }

    public static Project makeProject() {
        return new Project(generateRandomString(10));
    }

    public static Project makeProject(String name) {
        return new Project(name);
    }

    public static List<Project> makeProjects() {
        return makeProjects(3);
    }

    public static List<Project> makeProjects(int size) {
        List<Project> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeProject());
        }
        return list;
    }

    public static ProjectMemberResult makeProjectMembers(String nickname, String email, String password) {
        Member member = makeMember(nickname, email, password);
        List<Project> projects = makeProjects();

        List<ProjectMember> projectMembers = new ArrayList<>();
        for (Project project : projects) {
            projectMembers.add(makeProjectMember(project, member));
        }
        return new ProjectMemberResult(member, projects, projectMembers);
    }

    public static ProjectMemberResult makeProjectMembers() {
        Member member = makeMember();
        List<Project> projects = makeProjects();

        List<ProjectMember> projectMembers = new ArrayList<>();
        for (Project project : projects) {
            projectMembers.add(makeProjectMember(project, member));
        }
        return new ProjectMemberResult(member, projects, projectMembers);
    }

    public static ProjectMember makeProjectMember(Project project, Member member) {
        return new ProjectMember(project, member, USER);
    }

    public static ProjectMember makeProjectMember(Project project, Member member, ProjectRole projectRole) {
        return new ProjectMember(project, member, projectRole);
    }

    public static MockHttpSession makeMockSession() {
        return makeSession(generateNaturalNumber(1000), "ROLE_USER");
    }

    public static MockHttpSession makeMockSession(Long memberId) {
        return makeSession(memberId, "ROLE_USER");
    }

    public static MockHttpSession makeSession(Long memberId, String ROLE) {
        MockHttpSession session = new MockHttpSession();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", context);
        session.setAttribute("memberId", memberId);
        return session;
    }
}
