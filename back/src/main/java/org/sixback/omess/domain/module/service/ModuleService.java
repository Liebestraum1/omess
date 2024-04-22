package org.sixback.omess.domain.module.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.dto.response.GetModuleCategoryResponse;
import org.sixback.omess.domain.module.model.dto.response.GetModuleResponse;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.model.entity.ModuleCategory;
import org.sixback.omess.domain.module.repository.ModuleCategoryRepository;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleCategoryRepository moduleCategoryRepository;

    public List<GetModuleResponse> getModules(Long projectId) {
        // FixMe 요청자가 팀원인지 확인 로직 추가

        List<Module> modules = moduleRepository.findByProjectID(projectId);

        return modules.stream().map(
                module -> GetModuleResponse.builder()
                        .id(module.getId())
                        .title(module.getTitle())
                        .categoty(module.getCategory())
                        .build()
        ).toList();
    }

    @Transactional
    public void updateModule(Long module_id, String title) {
        // FixMe 요청자가 팀원인지 확인 로직 추가

        // FixMe 예외 처리
        Module module = moduleRepository.findById(module_id).orElseThrow(() -> new EntityNotFoundException());

        module.updateModule(title);

    }

    public List<GetModuleCategoryResponse> getModuleCategories() {
        List<ModuleCategory> moduleCategories = moduleCategoryRepository.findAll();

        return moduleCategories.stream().map(
                moduleCategory -> GetModuleCategoryResponse.builder()
                        .id(moduleCategory.getId())
                        .category(moduleCategory.getCategory())
                        .path(moduleCategory.getPath())
                        .build()
        ).toList();
    }
}
