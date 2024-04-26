package org.sixback.omess.domain.module.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.dto.request.UpdateMouleRequest;
import org.sixback.omess.domain.module.model.dto.response.GetModuleCategoryResponse;
import org.sixback.omess.domain.module.model.dto.response.GetModuleResponse;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.model.entity.ModuleCategory;
import org.sixback.omess.domain.module.repository.ModuleCategoryRepository;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleCategoryRepository moduleCategoryRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public List<GetModuleResponse> getModules(Long projectId, Long memberId) {
        isProjectMember(projectId, memberId);

        List<Module> modules = moduleRepository.findByProjectId(projectId);

        return modules.stream().map(
                module -> GetModuleResponse.builder()
                        .id(module.getId())
                        .title(module.getTitle())
                        .categoty(module.getCategory())
                        .build()
        ).toList();
    }

    @Transactional
    public void updateModule(Long memberId, Long moduleId, Long projectId, UpdateMouleRequest updateMouleRequest) {
        isProjectMember(projectId, memberId);

        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException("잘못된 요청 입니다."));

        module.updateModule(updateMouleRequest.getTitle());

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

    // 프로젝트 멤버 유효성 검사
    private void isProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException());
    }
}
