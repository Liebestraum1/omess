package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueDetailResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.member.model.dto.response.GetIssueResponses;
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

    @DeleteMapping("/{module_id}/issues/{issue_id}")
    public ResponseEntity<Void> deleteIssue(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @PathVariable("issue_id") Long issue_id) {

        kanbanBoardService.deleteIssue(memberId, projectId, moduleId, issue_id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}")
    public ResponseEntity<Void> updateIssue(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @PathVariable("issue_id") Long issue_id,
                                            @RequestBody UpdateIssueRequest updateIssueRequest) {

        kanbanBoardService.updateIssue(memberId, projectId, moduleId, issue_id, updateIssueRequest);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/member")
    public ResponseEntity<Void> updateIssueMember(@SessionAttribute(name = "memberId") Long memberId,
                                                  @PathVariable("project_id") Long projectId,
                                                  @PathVariable("module_id") Long moduleId,
                                                  @PathVariable("issue_id") Long issue_id,
                                                  @RequestBody Long chargerId) {

        kanbanBoardService.updateIssueMember(memberId, projectId, moduleId, issue_id, chargerId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/importance")
    public ResponseEntity<Void> updateIssueImportance(@SessionAttribute(name = "memberId") Long memberId,
                                                      @PathVariable("project_id") Long projectId,
                                                      @PathVariable("module_id") Long moduleId,
                                                      @PathVariable("issue_id") Long issue_id,
                                                      @RequestBody Integer importance) {

        kanbanBoardService.updateIssueImportance(memberId, projectId, moduleId, issue_id, importance);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/status")
    public ResponseEntity<Void> updateIssueStatus(@SessionAttribute(name = "memberId") Long memberId,
                                                  @PathVariable("project_id") Long projectId,
                                                  @PathVariable("module_id") Long moduleId,
                                                  @PathVariable("issue_id") Long issue_id,
                                                  @RequestBody Integer status) {

        kanbanBoardService.updateIssueStatus(memberId, projectId, moduleId, issue_id, status);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/label")
    public ResponseEntity<Void> updateIssueLabel(@SessionAttribute(name = "memberId") Long memberId,
                                                 @PathVariable("project_id") Long projectId,
                                                 @PathVariable("module_id") Long moduleId,
                                                 @PathVariable("issue_id") Long issue_id,
                                                 @RequestBody Long labelId) {

        kanbanBoardService.updateIssueLabel(memberId, projectId, moduleId, issue_id, labelId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{module_id}/issues")
    public ResponseEntity<GetIssueResponses> getIssues(@SessionAttribute(name = "memberId") Long memberId,
                                                       @PathVariable("project_id") Long projectId,
                                                       @PathVariable("module_id") Long moduleId,
                                                       @RequestParam(required = false) Long chargerId,
                                                       @RequestParam(required = false) Long labelId,
                                                       @RequestParam(required = false) Integer importance) {

        GetIssueResponses getIssueResponses =  kanbanBoardService.getIssues(memberId, projectId, moduleId, chargerId, labelId, importance);

        return ResponseEntity.ok().body(getIssueResponses);
    }

    @GetMapping("/{module_id}/issues/{issue_id}")
    public ResponseEntity<GetIssueDetailResponse> getIssue(@SessionAttribute(name = "memberId") Long memberId,
                                                           @PathVariable("project_id") Long projectId,
                                                           @PathVariable("module_id") Long moduleId,
                                                           @PathVariable("issue_id") Long issue_id){

        GetIssueDetailResponse getIssueDetailResponse = kanbanBoardService.getIssue(memberId, projectId, moduleId, issue_id);

        return ResponseEntity.ok().body(getIssueDetailResponse);
    }

}
