package org.sixback.omess.domain.kanbanboard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueDetailResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetLabelResponse;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.sixback.omess.domain.kanbanboard.repository.IssueRepository;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.repository.LabelRepository;
import org.sixback.omess.domain.member.model.dto.response.GetIssueResponses;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        KanbanBoard kanbanBoard = new KanbanBoard(writeKanbanBoardRequest.getTitle(), "kanbanboards", project);

        kanbanBoardRepository.save(kanbanBoard);

        String path = makeKanbanBoardPath(projectId, kanbanBoard.getId());

        kanbanBoard.updatePath(path);
    }

    @Transactional
    public void deleteKanbanBoard(Long memberId, Long moduleId, Long projectId) {
        Optional<KanbanBoard> findKanbanBaord = kanbanBoardRepository.findById(moduleId);

        String path = makeKanbanBoardPath(projectId, moduleId);

        if (findKanbanBaord.isPresent() && findKanbanBaord.get().getPath().equals(path)) {
            kanbanBoardRepository.deleteById(moduleId);
        } else {
            //FixMe 잘못된 요청입니다 에러 반환
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public GetKanbanBoardResponse getKanbanBoard(Long memberId, Long moduleId, Long projectId) {

        String path = makeKanbanBoardPath(projectId, moduleId);

        KanbanBoard findKanbanBoard = kanbanBoardRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException());

        if (findKanbanBoard.getPath().equals(path)) {
            List<Issue> issues = issueRepository.findByModuleId(moduleId);

            return GetKanbanBoardResponse.builder()
                    .moduleId(findKanbanBoard.getId())
                    .title(findKanbanBoard.getTitle())
                    .category(findKanbanBoard.getCategory())
                    .issues(issues.stream().map(
                                    issue -> GetIssueResponse.builder()
                                            .issueId(issue.getId())
                                            .charger(issue.getCharger() != null ? new GetMemberResponse(issue.getCharger().getId(), issue.getCharger().getNickname(), issue.getCharger().getEmail()) : null)
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
        } else {
            // FixMe 잘못된 요청 에러 던지기
            throw new EntityNotFoundException();
        }

    }

    @Transactional
    public void createIssue(Long projectId, Long moduleId, WriteIssueRequest writeIssueRequest) {
        Integer status = writeIssueRequest.getStatus();
        Integer importance = writeIssueRequest.getImportance();

        if (status == null || status > 3 || status < 1
                || importance == null || importance > 3 || importance < 0) {
            throw new EntityNotFoundException();
        }

        Member member = isProjectMember(projectId, writeIssueRequest.getMemberId());

        Label label = isModuleLabel(moduleId, writeIssueRequest.getLabelId());

        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException());

        Issue issue = new Issue(
                writeIssueRequest.getTitle(),
                writeIssueRequest.getContent(),
                writeIssueRequest.getImportance(),
                writeIssueRequest.getStatus(),
                kanbanBoard, member, label);

        issueRepository.save(issue);

        String path = makeIssuePath(projectId, moduleId, issue.getId());
        issue.updatePath(path);

    }

    @Transactional
    public void deleteIssue(Long memberId, Long projectId, Long moduleId, Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            issueRepository.delete(issue);
        }
    }

    @Transactional
    public void updateIssue(Long memberId, Long projectId, Long moduleId, Long issueId, UpdateIssueRequest updateIssueRequest) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            issue.updateIssue(updateIssueRequest);
        }
    }

    @Transactional
    public void updateIssueMember(Long memberId, Long projectId, Long moduleId, Long issueId, Long chargerId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            Member member = isProjectMember(projectId, chargerId);
            issue.updateMember(member);
        }
    }

    @Transactional
    public void updateIssueImportance(Long memberId, Long projectId, Long moduleId, Long issueId, Integer importance) {
        if (importance == null || importance > 3 || importance < 0) {
            throw new EntityNotFoundException();
        }

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            issue.updateImportance(importance);
        }
    }

    @Transactional
    public void updateIssueStatus(Long memberId, Long projectId, Long moduleId, Long issueId, Integer status) {
        if (status == null || status > 3 || status < 1) {
            throw new EntityNotFoundException();
        }

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            issue.updateStatus(status);
        }
    }

    @Transactional
    public void updateIssueLabel(Long memberId, Long projectId, Long moduleId, Long issueId, Long labelId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());
        String path = makeIssuePath(projectId, moduleId, issueId);

        if (issue.getPath().equals(path)) {
            issue.updateLabel(isModuleLabel(moduleId, labelId));
        }
    }

    @Transactional
    public GetIssueResponses getIssues(Long memberId, Long projectId, Long moduleId, Long chargerId, Long labelId, Integer importance) {
        String path = makeKanbanBoardPath(projectId, moduleId);

        Optional<KanbanBoard> findKanbanBard = kanbanBoardRepository.findById(moduleId);

        if (findKanbanBard.isPresent() && findKanbanBard.get().getPath().equals(path)) {
            List<Issue> findIssues = issueRepository.getIssues(path, chargerId, labelId, importance);
            return GetIssueResponses.builder()
                    .issues(
                            findIssues.stream().map(
                                    issue -> GetIssueResponse.builder()
                                            .issueId(issue.getId())
                                            .charger(issue.getCharger() != null ? new GetMemberResponse(issue.getCharger().getId(), issue.getCharger().getNickname(), issue.getCharger().getEmail()) : null)
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
        } else {
            // FixMe 잘못된 요청 에러 던지기
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public GetIssueDetailResponse getIssue(Long memberId, Long projectId, Long moduleId, Long issueId) {

        String path = makeIssuePath(projectId, moduleId, issueId);

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException());

        if (issue.getPath().equals(path)) {
            return GetIssueDetailResponse.builder()
                    .issueId(issue.getId())
                    .charger(issue.getCharger() != null ? new GetMemberResponse(issue.getCharger().getId(), issue.getCharger().getNickname(), issue.getCharger().getEmail()) : null)
                    .label(issue.getLabel() != null ? GetLabelResponse.builder()
                            .labelId(issue.getLabel().getId())
                            .name(issue.getLabel().getName())
                            .build() : null)
                    .title(issue.getTitle())
                    .content(issue.getContent())
                    .importance(issue.getImportance())
                    .status(issue.getStatus())
                    .build();
        } else {
            // FixMe 잘못된 요청 에러 던지기
            throw new EntityNotFoundException();
        }
    }

    // FixMe 에러 처리 수정
    // 프로젝트 멤버 유효성 검사
    private Member isProjectMember(Long projectId, Long memberId) {
        if (memberId == null) {
            return null;
        }
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException());

        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    // 라벨 검사 존재하는 라벨인지 -> 해당 모듈에 포함된 라벨인지
    private Label isModuleLabel(Long moduleId, Long labelId) {
        if (labelId == null) {
            return null;
        }

        Label label = labelRepository.findById(labelId).orElseThrow(() -> new EntityNotFoundException());

        if (label.getKanbanBoard().getId().equals(moduleId)) {
            return label;
        } else {
            //FixMe 에러 수정
            throw new EntityNotFoundException();
        }
    }

    // FixMe 에러 처리 수정
    // 프로젝트 조회
    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException());
    }

    private String makeKanbanBoardPath(Long projectId, Long kanbanBoardId) {
        return "P" + projectId + "/K" + kanbanBoardId;
    }

    private String makeIssuePath(Long projectId, Long kanbanboardId, Long issueId) {
        return "P" + projectId + "/K" + kanbanboardId + "/I" + issueId;
    }

    private String makeLabelePath(Long projectId, Long kanbanboardId, Long labelId) {
        return "P" + projectId + "/K" + kanbanboardId + "/L" + labelId;
    }
}
