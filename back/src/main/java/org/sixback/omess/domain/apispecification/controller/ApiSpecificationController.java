package org.sixback.omess.domain.apispecification.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.service.ApiSpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api-specifications")
@RequiredArgsConstructor
public class ApiSpecificationController {
    private final ApiSpecificationService apiSpecificationService;

    @PostMapping
    public ResponseEntity<Void> addApiSpecification(
            @SessionAttribute(name = "memberId") Long memberId,
            @RequestBody CreateApiSpecificationRequest createApiSpecificationRequest
    ) {
        apiSpecificationService.createApiSpecification(memberId, createApiSpecificationRequest);
        return ResponseEntity.ok().build();
    }
}
