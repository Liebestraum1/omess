package org.sixback.omess.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/api/v1/projects")
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<CreateProjectResponse> createProject(
            @RequestBody
            @Validated
            CreateProjectRequest createProjectRequest,
            @SessionAttribute(name = "memberId") Long memberId
    ) {
        System.out.println("createProjectRequest = " + createProjectRequest);
        CreateProjectResponse createProjectResponse = projectService.createProject(createProjectRequest, memberId);
        return new ResponseEntity<>(createProjectResponse, CREATED);
    }

}
