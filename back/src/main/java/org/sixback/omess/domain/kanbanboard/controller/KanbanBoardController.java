package org.sixback.omess.domain.kanbanboard.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueCommonRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.label.WriteLabelRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueDetailResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.label.GetLabelResponses;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.member.model.dto.response.GetIssueResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{project_id}/kanbanboards")
@RequiredArgsConstructor
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;

    @PostMapping
    public ResponseEntity<Void> createKanbanBoard(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("project_id") Long projectId,
            @RequestBody @Validated WriteKanbanBoardRequest writeKanbanBoardRequest) {
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
                                            @RequestBody @Validated WriteIssueRequest writeIssueRequest) {
        System.out.println(writeIssueRequest);
        kanbanBoardService.createIssue(memberId, projectId, moduleId, writeIssueRequest);

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
                                            @RequestBody @Validated UpdateIssueRequest updateIssueRequest) {

        kanbanBoardService.updateIssue(memberId, projectId, moduleId, issue_id, updateIssueRequest);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/member")
    public ResponseEntity<Void> updateIssueMember(@SessionAttribute(name = "memberId") Long memberId,
                                                  @PathVariable("project_id") Long projectId,
                                                  @PathVariable("module_id") Long moduleId,
                                                  @PathVariable("issue_id") Long issue_id,
                                                  @RequestBody UpdateIssueCommonRequest updateIssueCommonRequest) {

        kanbanBoardService.updateIssueMember(memberId, projectId, moduleId, issue_id, updateIssueCommonRequest.getChargerId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/importance")
    public ResponseEntity<Void> updateIssueImportance(@SessionAttribute(name = "memberId") Long memberId,
                                                      @PathVariable("project_id") Long projectId,
                                                      @PathVariable("module_id") Long moduleId,
                                                      @PathVariable("issue_id") Long issue_id,
                                                      @RequestBody @Validated UpdateIssueCommonRequest updateIssueCommonRequest) {

        kanbanBoardService.updateIssueImportance(memberId, projectId, moduleId, issue_id, updateIssueCommonRequest.getImportance());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/status")
    public ResponseEntity<Void> updateIssueStatus(@SessionAttribute(name = "memberId") Long memberId,
                                                  @PathVariable("project_id") Long projectId,
                                                  @PathVariable("module_id") Long moduleId,
                                                  @PathVariable("issue_id") Long issue_id,
                                                  @RequestBody @Validated UpdateIssueCommonRequest updateIssueCommonRequest) {

        kanbanBoardService.updateIssueStatus(memberId, projectId, moduleId, issue_id, updateIssueCommonRequest.getStatus());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{module_id}/issues/{issue_id}/label")
    public ResponseEntity<Void> updateIssueLabel(@SessionAttribute(name = "memberId") Long memberId,
                                                 @PathVariable("project_id") Long projectId,
                                                 @PathVariable("module_id") Long moduleId,
                                                 @PathVariable("issue_id") Long issue_id,
                                                 @RequestBody UpdateIssueCommonRequest updateIssueCommonRequest) {

        kanbanBoardService.updateIssueLabel(memberId, projectId, moduleId, issue_id, updateIssueCommonRequest.getLabelId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{module_id}/issues")
    public ResponseEntity<GetIssueResponses> getIssues(@SessionAttribute(name = "memberId") Long memberId,
                                                       @PathVariable("project_id") Long projectId,
                                                       @PathVariable("module_id") Long moduleId,
                                                       @RequestParam(required = false) Long chargerId,
                                                       @RequestParam(required = false) Long labelId,
                                                       @RequestParam(required = false)  @Max(3) @Min(0) @Validated Integer importance) {

        GetIssueResponses getIssueResponses = kanbanBoardService.getIssues(memberId, projectId, moduleId, chargerId, labelId, importance);

        return ResponseEntity.ok().body(getIssueResponses);
    }

    @GetMapping("/{module_id}/issues/{issue_id}")
    public ResponseEntity<GetIssueDetailResponse> getIssue(@SessionAttribute(name = "memberId") Long memberId,
                                                           @PathVariable("project_id") Long projectId,
                                                           @PathVariable("module_id") Long moduleId,
                                                           @PathVariable("issue_id") Long issue_id) {

        GetIssueDetailResponse getIssueDetailResponse = kanbanBoardService.getIssue(memberId, projectId, moduleId, issue_id);

        return ResponseEntity.ok().body(getIssueDetailResponse);
    }

    @PostMapping("/{module_id}/label")
    public ResponseEntity<Void> createLabel(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @RequestBody @Validated WriteLabelRequest writeLabelRequest) {

        kanbanBoardService.createLabel(memberId, projectId, moduleId, writeLabelRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{module_id}/label/{label_id}")
    public ResponseEntity<Void> deleteLabel(@SessionAttribute(name = "memberId") Long memberId,
                                            @PathVariable("project_id") Long projectId,
                                            @PathVariable("module_id") Long moduleId,
                                            @PathVariable("label_id") Long labelId) {

        kanbanBoardService.deleteLabel(memberId, projectId, moduleId, labelId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{module_id}/label")
    public ResponseEntity<GetLabelResponses> getLabels(@SessionAttribute(name = "memberId") Long memberId,
                                                       @PathVariable("project_id") Long project_id,
                                                       @PathVariable("module_id") Long module_id){
        return ResponseEntity.ok().body(kanbanBoardService.getLabels(memberId, project_id, module_id));
    }

}
