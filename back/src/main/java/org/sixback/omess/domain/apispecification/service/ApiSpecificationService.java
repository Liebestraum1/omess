package org.sixback.omess.domain.apispecification.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateDomainRequest;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.repository.DomainRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import static org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils.generatePath;

@Service
@RequiredArgsConstructor
public class ApiSpecificationService {
    private final ApiSpecificationRepository apiSpecificationRepository;
    private final ProjectRepository projectRepository;
    private final DomainRepository domainRepository;

    @Transactional
    public void createApiSpecification(Long projectId, CreateApiSpecificationRequest createApiSpecificationRequest, String uri) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 프로젝트입니다."));

        ApiSpecification apiSpecification = ApiSpecification.builder()
                .moduleName(createApiSpecificationRequest.name())
                .moduleCategory(createApiSpecificationRequest.category())
                .project(project)
                .build();

        ApiSpecification savedApiSpecification = apiSpecificationRepository.save(apiSpecification);
        savedApiSpecification.addPath(generatePath(uri, savedApiSpecification.getId()));

        apiSpecificationRepository.save(savedApiSpecification);
    }


    @Transactional
    public void createDomain(Long apiSpecificationId, CreateDomainRequest createDomainRequest) {
        ApiSpecification apiSpecification = apiSpecificationRepository.findById(apiSpecificationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 API 명세서입니다."));

        Domain domain = new Domain(createDomainRequest.name(), apiSpecification);

        domainRepository.save(domain);
    }
}
