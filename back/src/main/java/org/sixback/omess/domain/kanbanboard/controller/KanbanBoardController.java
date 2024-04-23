package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projects_id}/kanbanboards")
@RequiredArgsConstructor
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;

    @PostMapping
    public ResponseEntity<Void> createKanbanBoard(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("project_id") Long projectId,
            @RequestBody WriteKanbanBoardRequest writeKanbanBoardRequest) {
        kanbanBoardService.createKanbanBoard(memberId, projectId, writeKanbanBoardRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{module_id}")
    public ResponseEntity<Void> deleteKanbanBoard(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("module_id") Long moduleId,
            @PathVariable("project_id") Long projectId) {
        kanbanBoardService.deleteKanbanBoard(memberId, moduleId, projectId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{module_id}")
    public ResponseEntity<GetKanbanBoardResponse> getKanbanBoardResponse(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("module_id") Long moduleId,
            @PathVariable("project_id") Long projectId) {
        return ResponseEntity.ok().body(kanbanBoardService.getKanbanBoard(memberId, moduleId, projectId));
    }

    @PostMapping("/{module_id}/issues")
    public ResponseEntity<Void> createIssue(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @RequestBody WriteIssueRequest writeIssueRequest) {
        kanbanBoardService.createIssue(projectId, moduleId, writeIssueRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{module_id}/issue/{issue_id}")
    public ResponseEntity<Void> deleteIssue(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @PathVariable("issue_id") Long issue_id){

        kanbanBoardService.deleteIssue(memberId, projectId, moduleId, issue_id);

        return ResponseEntity.ok().build();
    }
}
