package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanBoardStompController {

    @MessageMapping("/kanbanboards/{module_id}")
    @SendTo("/sub/kanbanRoom/{module_id}")
    public String updateIssue(@DestinationVariable("module_id") Long moduleId) {
        return moduleId.toString();
    }

    @MessageMapping("/kanbanboards/{module_id}/label")
    @SendTo("/sub/kanbanRoom/{module_id}/label")
    public String updateLabel(@DestinationVariable("module_id") Long moduleId) {
        return moduleId.toString();
    }
}
