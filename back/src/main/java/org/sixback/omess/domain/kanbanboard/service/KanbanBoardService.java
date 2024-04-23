package org.sixback.omess.domain.kanbanboard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetIssueResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetLabelResponse;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.IssueRepository;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KanbanBoardService {
    private final KanbanBoardRepository kanbanBoardRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public void createKanbanBoard(Long memberId, Long projectId, WriteKanbanBoardRequest writeKanbanBoardRequest) {
        isProjectMember(projectId, memberId);

        Project project = getProject(projectId);

        KanbanBoard kanbanBoard = new KanbanBoard(writeKanbanBoardRequest.getTitle(), "KanbanBoard", project);

        kanbanBoardRepository.save(kanbanBoard);
    }

    @Transactional
    public void deleteKanbanBoard(Long memberId, Long moduleId, DeleteKanbanBoardRequest deleteKanbanBoardRequest) {
        isProjectMember(deleteKanbanBoardRequest.getProjectId(), memberId);

        kanbanBoardRepository.deleteById(moduleId);
    }

    @Transactional
    public GetKanbanBoardResponse getKanbanBoard(Long memberId, Long moduleId, Long projectId) {
        isProjectMember(projectId, memberId);

        KanbanBoard findKanbanBoard = kanbanBoardRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException());

        List<Issue> issues = issueRepository.findByModuleId(moduleId);

        return GetKanbanBoardResponse.builder()
                .moduleId(findKanbanBoard.getId())
                .title(findKanbanBoard.getTitle())
                .category(findKanbanBoard.getCategory())
                .issues(issues.stream().map(
                                issue -> GetIssueResponse.builder()
                                        .issueId(issue.getId())
                                        .label(issue.getLabel() != null ? GetLabelResponse.builder()
                                                .labelId(issue.getLabel().getId())
                                                .name(issue.getLabel().getName())
                                                .build() : null)
                                        .title(issue.getTitle())
                                        .importance(issue.getImportance())
                                        .status(issue.getStatus())
                                        .build()
                        ).toList()
                )
                .build();
    }

    // FixMe 에러 처리 수정
    // 프로젝트 멤버 유효성 검사
    private void isProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    // FixMe 에러 처리 수정
    // 프로젝트 조회
    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException());
    }

    // FixMe 에러 처리 수정
    // 멤버 조회
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }
}
