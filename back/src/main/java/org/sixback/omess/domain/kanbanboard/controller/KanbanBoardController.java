package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kanbanboards")
@RequiredArgsConstructor
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;

    @PostMapping("/{project_id}")
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
            @RequestBody DeleteKanbanBoardRequest deleteKanbanBoardRequest){
        kanbanBoardService.deleteKanbanBoard(memberId, moduleId, deleteKanbanBoardRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{module_id}")
    public ResponseEntity<GetKanbanBoardResponse> getKanbanBoardResponse(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("module_id") Long moduleId,
            @RequestBody Long projectId){
        return ResponseEntity.ok().body(kanbanBoardService.getKanbanBoard(memberId, moduleId, projectId));
    }
}
