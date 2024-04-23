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
@RequestMapping("/api/v1/projects/{project_id}/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<List<GetModuleResponse>> getModules(@SessionAttribute(name = "memberId") Long memberId, @PathVariable("project_id") Long projectId) {
        return ResponseEntity.ok().body(moduleService.getModules(projectId, memberId));
    }

    @PatchMapping("/{module_id}")
    public ResponseEntity<Void> updateModule(@SessionAttribute(name = "memberId") Long memberId, @PathVariable("project_id") Long projectId, @PathVariable("module_id") Long moduleId, @RequestBody UpdateMouleRequest updateMouleRequest) {
        moduleService.updateModule(memberId, moduleId, projectId, updateMouleRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/modulecategory")
    public ResponseEntity<List<GetModuleCategoryResponse>> getModuleCategories(@SessionAttribute(name = "memberId") Long memberId, @PathVariable("project_id") Long projectId) {
        return ResponseEntity.ok().body(moduleService.getModuleCategories());
    }

}
