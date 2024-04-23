package org.sixback.omess.domain.apispecification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/api-specifications")
@RequiredArgsConstructor
public class ApiSpecificationController {
    private final ApiSpecificationService apiSpecificationService;

    @PostMapping
    public ResponseEntity<Void> addApiSpecification(
            @SessionAttribute(name = "memberId") Long memberId, //FIXME : 추후 Project에 속한 Member인지 확인하는 AOP or Filter로 대체
            @PathVariable Long projectId,
            @Valid @RequestBody CreateApiSpecificationRequest createApiSpecificationRequest
    ) {
        apiSpecificationService.createApiSpecification(memberId, projectId, createApiSpecificationRequest);
        return ResponseEntity.ok().build();
    }
}
