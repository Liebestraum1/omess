package org.sixback.omess.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.service.MemberService;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.model.enums.ProjectRole;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sixback.omess.domain.member.mapper.MemberMapper.toMember;
import static org.sixback.omess.domain.project.mapper.ProjectMapper.toCreateProjectResponse;
import static org.sixback.omess.domain.project.mapper.ProjectMapper.toProject;
import static org.sixback.omess.domain.project.model.enums.ProjectRole.OWNER;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final MemberService memberService;

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional(readOnly = true)
    public CreateProjectResponse createProject(CreateProjectRequest createProjectRequest, Long memberId) {
        Member foundMember = toMember(memberService.getMember(memberId));
        Project createdProject = projectRepository.save(toProject(createProjectRequest));
        projectMemberRepository.save(new ProjectMember(createdProject, foundMember, OWNER));
        return toCreateProjectResponse(createdProject);
    }
}
