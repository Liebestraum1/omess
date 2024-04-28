package org.sixback.omess.domain.apispecification.controller;

import org.sixback.omess.domain.apispecification.model.dto.CreateApiRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateDomainRequest;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/api-specifications")
@RequiredArgsConstructor
public class ApiSpecificationController {
    private final ApiSpecificationService apiSpecificationService;

    @PostMapping
    public ResponseEntity<Void> createApiSpecification(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateApiSpecificationRequest createApiSpecificationRequest,
            HttpServletRequest httpServletRequest
    ) {
        apiSpecificationService.createApiSpecification(projectId, createApiSpecificationRequest, httpServletRequest.getRequestURI());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{apiSpecificationId}/domains")
    public ResponseEntity<Void> createDomain(
            @PathVariable Long apiSpecificationId,
            @Valid @RequestBody CreateDomainRequest createDomainRequest,
            HttpServletRequest httpServletRequest
    ){
        apiSpecificationService.createDomain(apiSpecificationId, createDomainRequest, httpServletRequest.getRequestURI());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{apiSpecificationId}/domains/{domainId}/apis")
    public ResponseEntity<Void> createApi(
        @PathVariable Long domainId,
        @Valid @RequestBody CreateApiRequest createApiRequest,
        HttpServletRequest httpServletRequest
    ){
        apiSpecificationService.createApi(domainId, createApiRequest, httpServletRequest.getRequestURI());

        return ResponseEntity.ok().build();
    }
}
