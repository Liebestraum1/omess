package org.sixback.omess.domain.kanbanboard.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kanbanboards")
@RequiredArgsConstructor
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;
}
