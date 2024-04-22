package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kanbanboards")
@RequiredArgsConstructor
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;

    @PostMapping("/{project_id}")
    public void createKanbanBoard(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("project_id") Long projectId,
            @RequestBody WriteKanbanBoardRequest writeKanbanBoardRequest
    ) {
        kanbanBoardService.createKanbanBoard(memberId, projectId, writeKanbanBoardRequest);
    }

    @DeleteMapping("/{module_id}")
    public void deleteKanbanBoard(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("module_id") Long moduleId,
            @RequestBody DeleteKanbanBoardRequest deleteKanbanBoardRequest
            ){
        kanbanBoardService.deleteKanbanBoard(memberId, moduleId, deleteKanbanBoardRequest);
    }
}
