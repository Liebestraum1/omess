package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueResponse;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class KanbanBoardStompController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final KanbanBoardService kanbanBoardService;

    @MessageMapping("/projects/{project_id}/kanbanboards/{module_id}")
    public void updateIssue(@SessionAttribute(name = "memberId") Long memberId,
                                          @PathVariable("project_id") Long projectId,
                                          @PathVariable("module_id") Long moduleId,
                                          @RequestBody @Validated WriteIssueRequest writeIssueRequest) {
        GetIssueResponse issueStomp = kanbanBoardService.createIssueStomp(memberId, projectId, moduleId, writeIssueRequest);
        simpMessagingTemplate.convertAndSend("/sub/kanbanroom/" + issueStomp.issueId(), issueStomp);
    }
}
