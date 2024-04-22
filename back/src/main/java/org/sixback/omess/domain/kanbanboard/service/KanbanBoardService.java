package org.sixback.omess.domain.kanbanboard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KanbanBoardService {
    private final KanbanBoardRepository kanbanBoardRepository;
    private  final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void createKanbanBoard(Long memberId, Long projectId, WriteKanbanBoardRequest writeKanbanBoardRequest) {
        // FixMe 공통에러 처리
        if(!isProjectMember(projectId, memberId)){
            throw new EntityNotFoundException();
        }

        Project project = getProject(projectId);

        KanbanBoard kanbanBoard = new KanbanBoard(writeKanbanBoardRequest.getTitle(), "KanbanBoard", project);

        kanbanBoardRepository.save(kanbanBoard);
    }

    @Transactional
    public void deleteKanbanBoard(Long memberId, Long moduleId, DeleteKanbanBoardRequest deleteKanbanBoardRequest) {
        if(!isProjectMember(deleteKanbanBoardRequest.getProjectId(), memberId)){
            throw new EntityNotFoundException();
        }

        kanbanBoardRepository.deleteById(moduleId);
    }

    private boolean isProjectMember(Long projectId, Long memberId){
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).isPresent();
    }

    // FixMe 에러 처리 수정
    private Project getProject(Long projectId){
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException());
    }

    // FixMe 에러 처리 수정
    private Member getMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }
}
