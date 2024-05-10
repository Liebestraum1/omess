package org.sixback.omess.domain.project.mapper;

import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.project.dto.response.GetProjectMemberResponse;
import org.sixback.omess.domain.project.dto.response.GetProjectMembersResponse;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.model.response.GetMembersInProjectResponse;

import java.util.List;

import static org.sixback.omess.domain.project.model.enums.ProjectRole.OWNER;


public class ProjectMapper {
    public static Project toProject(CreateProjectRequest createProjectRequest) {
        return new Project(createProjectRequest.getName());
    }

    public static CreateProjectResponse toCreateProjectResponse(Project project) {
        return new CreateProjectResponse(project.getId(), project.getName(), OWNER);
    }

    public static GetProjectMembersResponse toGetProjectMembersResponse(List<ProjectMember> projectMembers) {
        List<GetProjectMemberResponse> list = projectMembers.stream()
                .map(ProjectMapper::toGetProjectResponse)
                .toList();
        return new GetProjectMembersResponse(list);
    }

    public static GetProjectMemberResponse toGetProjectResponse(ProjectMember projectMember) {
        return new GetProjectMemberResponse(projectMember.getProject().getId(), projectMember.getProject().getName());
    }

    public static GetMembersInProjectResponse toGetMembersInProjectResponse(ProjectMember projectMember) {
        Member member = projectMember.getMember();
        if (member == null) {
            return null;
        }
        return new GetMembersInProjectResponse(member.getId(), member.getNickname(), member.getProfile());
    }
}
