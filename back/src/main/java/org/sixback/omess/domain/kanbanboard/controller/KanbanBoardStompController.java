package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class KanbanBoardStompController {
    private final KanbanBoardService kanbanBoardService;

    @MessageMapping("/kanbanboards/{module_id}")
    @SendTo("/sub/kanbanRoom/{module_id}")
    public String updateIssue(
                            @DestinationVariable("module_id") Long moduleId,
                             WriteIssueRequest writeIssueRequest) {
        System.out.println(writeIssueRequest.getTitle());
       return writeIssueRequest.getTitle();
    }
}
