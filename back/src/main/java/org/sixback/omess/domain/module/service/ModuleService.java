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
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        // FixMe 요청자가 팀원인지 확인 로직 추가
        isProjectMember(projectId, memberId);

        // FixMe 예외 처리
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException());

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

    // FixMe 에러 처리 수정
    // 프로젝트 멤버 유효성 검사
    private void isProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException());
    }
}
