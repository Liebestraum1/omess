package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.label.GetLabelResponses;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.member.model.dto.response.GetIssueResponses;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanBoardStompController {
    private final KanbanBoardService kanbanBoardService;

    @MessageMapping("/projects/{project_id}/kanbanboards/{module_id}")
    @SendTo("/sub/projects/{project_id}/kanbanRoom/{module_id}")
    public GetIssueResponses updateIssue(@DestinationVariable("project_id") Long projectId,
                                         @DestinationVariable("module_id") Long moduleId) {
        GetIssueResponses getIssueResponses = kanbanBoardService.getIssuesAll(projectId, moduleId);

        return getIssueResponses;
    }

    @MessageMapping("/projects/{project_id}/kanbanboards/{module_id}/label")
    @SendTo("/sub/projects/{project_id}/kanbanRoom/{module_id}/label")
    public GetLabelResponses updateLabel(@DestinationVariable("project_id") Long projectId,
                                         @DestinationVariable("module_id") Long moduleId) {
        return kanbanBoardService.getLabels(0L, projectId, moduleId);
    }
}
