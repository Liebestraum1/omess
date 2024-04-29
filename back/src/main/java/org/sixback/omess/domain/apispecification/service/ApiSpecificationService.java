package org.sixback.omess.domain.apispecification.service;

import static org.sixback.omess.domain.apispecification.exception.ApiSpecificationErrorMessage.*;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationMapper.*;
import static org.sixback.omess.domain.apispecification.util.ApiSpecificationUtils.*;

import java.util.Optional;

import org.sixback.omess.domain.apispecification.exception.InvalidApiInputException;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateApiRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateDomainRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.UpdateDomainRequest;
import org.sixback.omess.domain.apispecification.model.dto.response.GetApiSpecificationResponse;
import org.sixback.omess.domain.apispecification.model.dto.response.GetDomainsResponse;
import org.sixback.omess.domain.apispecification.model.entity.Api;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.sixback.omess.domain.apispecification.repository.ApiRepository;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.apispecification.repository.DomainRepository;
import org.sixback.omess.domain.httpmethod.model.HttpMethod;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiSpecificationService {
    private final ApiSpecificationRepository apiSpecificationRepository;
    private final ProjectRepository projectRepository;
    private final DomainRepository domainRepository;
    private final ApiRepository apiRepository;

    @Transactional(readOnly = true)
    public GetApiSpecificationResponse getApiSpecification(Long apiSpecificationId, String uri) {
        String estimatedCurrentPath = generateEstimatedCurrentPath(uri);

        ApiSpecification apiSpecification = apiSpecificationRepository.findByPath(estimatedCurrentPath)
            .orElseThrow(() -> new EntityNotFoundException(PATH_MISMATCH.getMessage()));

        return toGetApiSpecificationResponse(apiSpecification);
    }

    @Transactional(readOnly = true)
    public GetDomainsResponse getDomains(Long apiSpecificationId, String uri) {
        String estimatedParentPath = generateEstimatedParentPath(uri, apiSpecificationId);

        ApiSpecification apiSpecification = apiSpecificationRepository.findByPath(estimatedParentPath)
            .orElseThrow(() -> new EntityNotFoundException(PATH_MISMATCH.getMessage()));

        return toGetDomainsResponse(apiSpecification.getDomains());
    }

    @Transactional
    public void createApiSpecification(Long projectId, CreateApiSpecificationRequest createApiSpecificationRequest, String uri) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 프로젝트입니다."));

        ApiSpecification apiSpecification = ApiSpecification.builder()
                .moduleName(createApiSpecificationRequest.name())
                .moduleCategory(createApiSpecificationRequest.category())
                .project(project)
                .build();

        apiSpecificationRepository.save(apiSpecification);
        apiSpecification.addPath(generatePath(uri, apiSpecification.getId()));
        apiSpecificationRepository.save(apiSpecification);
    }


    @Transactional
    public void createDomain(Long apiSpecificationId, CreateDomainRequest createDomainRequest, String uri) {
        String estimatedParentPath = generateEstimatedParentPath(uri, apiSpecificationId);

        ApiSpecification apiSpecification = apiSpecificationRepository.findByPath(estimatedParentPath)
                .orElseThrow(() -> new EntityNotFoundException(PATH_MISMATCH.getMessage()));

        Domain domain = Domain.builder()
                .name(createDomainRequest.name())
                .apiSpecification(apiSpecification)
                .build();

        domainRepository.save(domain);
        domain.addPath(generatePath(uri, domain.getId()));
        domainRepository.save(domain);
    }

    @Transactional
    public void createApi(Long domainId, CreateApiRequest createApiRequest, String uri) {
        if(!HttpMethod.getMethodNames().contains(createApiRequest.getMethod())) {
            throw new InvalidApiInputException(INVALID_HTTP_METHOD.getMessage());
        }

        String estimatedParentPath = generateEstimatedParentPath(uri, domainId);

        Domain domain = domainRepository.findByPath(estimatedParentPath)
            .orElseThrow(() -> new EntityNotFoundException(PATH_MISMATCH.getMessage()));

        checkIsValidJsonSchema(createApiRequest.getRequestSchema());
        checkIsValidJsonSchema(createApiRequest.getResponseSchema());

        Api api = Api.builder()
            .domain(domain)
            .method(createApiRequest.getMethod())
            .name(createApiRequest.getName())
            .description(createApiRequest.getDescription())
            .endpoint(createApiRequest.getEndpoint())
            .statusCode(createApiRequest.getStatusCode())
            .requestSchema(createApiRequest.getRequestSchema())
            .responseSchema(createApiRequest.getResponseSchema())
            .build();

        apiRepository.save(api);
        api.addPath(generatePath(uri, api.getId()));

        //FIXME API 수정 방식에 따라 하위 테이블에 path 추가 여부 결정
        Optional.ofNullable(createApiRequest.getCreatePathVariableRequests())
            .ifPresent(requests -> requests.forEach(request ->
                api.getPathVariables().add(toPathVariable(request, api))));

        Optional.ofNullable(createApiRequest.getCreateQueryParamRequests())
            .ifPresent(requests -> requests.forEach(request ->
                api.getQueryParams().add(toQueryParam(request, api))));

        Optional.ofNullable(createApiRequest.getCreateRequestHeaderRequests())
            .ifPresent(requests -> requests.forEach(request ->
                api.getRequestHeaders().add(toRequestHeader(request, api))));

        apiRepository.save(api);
    }

    @Transactional
    public void updateDomain(UpdateDomainRequest updateDomainRequest, String uri) {
        String estimatedCurrentPath = generateEstimatedCurrentPath(uri);

        Domain domain = domainRepository.findByPath(estimatedCurrentPath)
            .orElseThrow(() -> new EntityNotFoundException(PATH_MISMATCH.getMessage()));

        domain.updateName(updateDomainRequest.name());
        domainRepository.save(domain);
    }

    @Transactional
    public void deleteDomain(String uri) {
        String estimatedCurrentPath = generateEstimatedCurrentPath(uri);
        boolean exists = domainRepository.existsByPath(estimatedCurrentPath);

        if (!exists) {
            throw new EntityNotFoundException(PATH_MISMATCH.getMessage());
        }

        domainRepository.deleteByPath(estimatedCurrentPath);
    }

    @Transactional
    public void deleteApi(String uri) {
        String estimatedCurrentPath = generateEstimatedCurrentPath(uri);
        boolean exists = apiRepository.existsByPath(estimatedCurrentPath);

        if (!exists) {
            throw new EntityNotFoundException(PATH_MISMATCH.getMessage());
        }

        apiRepository.deleteByPath(estimatedCurrentPath);
    }
}
