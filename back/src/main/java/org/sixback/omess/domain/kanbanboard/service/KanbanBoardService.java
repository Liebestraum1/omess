package org.sixback.omess.domain.kanbanboard.service;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KanbanBoardService {
    private final KanbanBoardRepository kanbanBoardRepository;
}
