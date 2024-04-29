package org.sixback.omess.domain.apispecification.controller;

import org.sixback.omess.domain.apispecification.model.dto.request.CreateApiRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateDomainRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.UpdateDomainRequest;
import org.sixback.omess.domain.apispecification.model.dto.response.GetDomainsResponse;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/{apiSpecificationId}")
    public ResponseEntity<?> getApiSpecification(
        @PathVariable Long apiSpecificationId,
        HttpServletRequest httpServletRequest
    ){
        return ResponseEntity.ok().body(apiSpecificationService.getApiSpecification(apiSpecificationId, httpServletRequest.getRequestURI()));
    }

    @GetMapping("/{apiSpecificationId}/domains")
    public ResponseEntity<GetDomainsResponse> getDomains(
        @PathVariable Long apiSpecificationId,
        HttpServletRequest httpServletRequest
    ){
        return ResponseEntity.ok().body(apiSpecificationService.getDomains(apiSpecificationId, httpServletRequest.getRequestURI()));
    }

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

    @PatchMapping("/{apiSpecificationId}/domains/{domainId}")
    public ResponseEntity<Void> updateDomain(
        @Valid @RequestBody UpdateDomainRequest updateDomainRequest,
        HttpServletRequest httpServletRequest
    ){
        apiSpecificationService.updateDomain(updateDomainRequest ,httpServletRequest.getRequestURI());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{apiSpecificationId}/domains/{domainId}")
    public ResponseEntity<Void> deleteDomain(
        HttpServletRequest httpServletRequest
    ){
        apiSpecificationService.deleteDomain(httpServletRequest.getRequestURI());

        return ResponseEntity.ok().build();
    }
}
