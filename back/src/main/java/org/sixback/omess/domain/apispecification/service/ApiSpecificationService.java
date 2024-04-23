package org.sixback.omess.domain.apispecification.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiSpecificationService {
    private final ApiSpecificationRepository apiSpecificationRepository;
    //FIXME : 추후 Project에 속한 Member인지 확인하는 AOP or Filter로 대체
    private final ProjectMemberRepository projectMemberRepository;
    //FIXME : 추후 ProjectService에서 제공하는 method로 대체
    private final ProjectRepository projectRepository;

    @Transactional
    public void createApiSpecification(Long memberId, Long projectId, CreateApiSpecificationRequest createApiSpecificationRequest) {
        checkIsProjectMember(memberId, projectId);

        //FIXME : 추후 ProjectService에서 제공하는 method로 대체
        Project project = projectRepository.findById(projectId).get();

        ApiSpecification apiSpecification = ApiSpecification.builder()
                .moduleName(createApiSpecificationRequest.name())
                .moduleCategory(createApiSpecificationRequest.category())
                .project(project)    //FIXME : 추후 ProjectService에서 제공하는 method로 대체
                .build();

        apiSpecificationRepository.save(apiSpecification);
    }


    //FIXME : 추후 Project에 속한 Member인지 확인하는 AOP or Filter로 대체
    private void checkIsProjectMember(Long memberId, Long projectId){
        projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트의 멤버가 아닙니다."));
    }
}
