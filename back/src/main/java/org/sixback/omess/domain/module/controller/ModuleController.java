package org.sixback.omess.domain.module.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.dto.response.GetModuleCategoryResponse;
import org.sixback.omess.domain.module.model.dto.response.GetModuleResponse;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/project_id")
    public List<GetModuleResponse> getModules(@PathVariable("project_id")Long projectId){
        return moduleService.getModules(projectId);
    }

    @PatchMapping("/module_id")
    public void updateModule(@PathVariable("module_id")Long module_id, @RequestBody String title){
        moduleService.updateModule(module_id, title);
    }

    @GetMapping
    public List<GetModuleCategoryResponse> getModuleCategories(){
        return moduleService.getModuleCategories();
    }

}
