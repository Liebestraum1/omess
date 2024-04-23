package org.sixback.omess.domain.module.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.dto.request.UpdateMouleRequest;
import org.sixback.omess.domain.module.model.dto.response.GetModuleCategoryResponse;
import org.sixback.omess.domain.module.model.dto.response.GetModuleResponse;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/{project_id}")
    public ResponseEntity<List<GetModuleResponse>> getModules(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("project_id") Long projectId) {
        return ResponseEntity.ok().body(moduleService.getModules(projectId, memberId));
    }

    @PatchMapping("/{module_id}")
    public ResponseEntity<Void> updateModule(
            @SessionAttribute(name = "memberId") Long memberId,
            @PathVariable("module_id") Long moduleId,
            @RequestBody UpdateMouleRequest updateMouleRequest) {
        moduleService.updateModule(memberId, moduleId, updateMouleRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetModuleCategoryResponse>> getModuleCategories() {
        return ResponseEntity.ok().body(moduleService.getModuleCategories());
    }

}
