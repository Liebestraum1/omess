package org.sixback.omess.domain.kanbanboard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetIssueResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetLabelResponse;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.sixback.omess.domain.kanbanboard.repository.IssueRepository;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.repository.LabelRepository;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.entity.Project;
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
    private final LabelRepository labelRepository;

    @Transactional
    public void createKanbanBoard(Long memberId, Long projectId, WriteKanbanBoardRequest writeKanbanBoardRequest) {
        Project project = getProject(projectId);

        KanbanBoard kanbanBoard = new KanbanBoard(writeKanbanBoardRequest.getTitle(), "KanbanBoard", project);

        kanbanBoardRepository.save(kanbanBoard);
    }

    @Transactional
    public void deleteKanbanBoard(Long memberId, Long moduleId, Long projectId) {
        kanbanBoardRepository.deleteById(moduleId);
    }

    @Transactional
    public GetKanbanBoardResponse getKanbanBoard(Long memberId, Long moduleId, Long projectId) {
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

    @Transactional
    public void createIssue(Long projectId, Long moduleId, WriteIssueRequest writeIssueRequest) {

        Member member =  isProjectMember(projectId, writeIssueRequest.getMemberId());

        Label label = isModuleLabel(moduleId, writeIssueRequest.getLabelId());

        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException());

        Issue issue = new Issue(writeIssueRequest.getTitle(), writeIssueRequest.getContent(), writeIssueRequest.getImportance(), writeIssueRequest.getStatus(), kanbanBoard, member, label);

        issueRepository.save(issue);
    }

    @Transactional
    public void deleteIssue(Long memberId, Long projectId, Long moduleId, Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());

        if(kanbanBoardRepository.findByIdAndProjectId(projectId, moduleId) != null && issue.getKanbanBoard().getId() == moduleId){
            issueRepository.delete(issue);
        }
    }

    // FixMe 에러 처리 수정
    // 프로젝트 멤버 유효성 검사
    private Member isProjectMember(Long projectId, Long memberId) {
        if(memberId == null){
            return null;
        }
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException());

        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    // 라벨 검사 존재하는 라벨인지 -> 해당 모듈에 포함된 라벨인지
    private Label isModuleLabel(Long moduleId, Long labelId) {
        if(labelId == null){
            return null;
        }

        Label label = labelRepository.findById(labelId).orElseThrow(() -> new EntityNotFoundException());

        if(label.getKanbanBoard().getId() == moduleId){
            return label;
        }else{
            //FixMe 에러 수정
            throw new EntityNotFoundException();
        }
    }

    // FixMe 에러 처리 수정
    // 프로젝트 조회
    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException());
    }
}
