package org.sixback.omess.domain.project.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.service.MemberService;
import org.sixback.omess.domain.project.dto.response.GetProjectMembersResponse;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.UpdateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sixback.omess.domain.member.mapper.MemberMapper.toMember;
import static org.sixback.omess.domain.project.mapper.ProjectMapper.*;
import static org.sixback.omess.domain.project.model.enums.ProjectRole.OWNER;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final MemberService memberService;

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public CreateProjectResponse createProject(CreateProjectRequest createProjectRequest, Long memberId) {
        Member foundMember = toMember(memberService.getMember(memberId));
        Project createdProject = projectRepository.save(toProject(createProjectRequest));
        projectMemberRepository.save(new ProjectMember(createdProject, foundMember, OWNER));
        return toCreateProjectResponse(createdProject);
    }

    @Transactional(readOnly = true)
    public GetProjectMembersResponse getProjects(Long memberId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByMember_Id(memberId);
        return toGetProjectMembersResponse(projectMembers);
    }

    @Transactional
    public void updateProject(Long projectId, UpdateProjectRequest updateProjectRequest) {
        Project project = getProjectEntity(projectId);
        project.updateProject(updateProjectRequest.getName());
    }

    @Transactional(readOnly = true)
    protected Project getProjectEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("")); //TODO
    }
}
