package org.sixback.omess.domain.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.project.dto.response.GetProjectMembersResponse;
import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.InviteProjectRequest;
import org.sixback.omess.domain.project.model.dto.request.UpdateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.model.response.GetMembersInProjectResponse;
import org.sixback.omess.domain.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RequestMapping("/api/v1/projects")
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(
            @Validated
            @RequestBody
            CreateProjectRequest createProjectRequest,
            @SessionAttribute(name = "memberId") Long memberId
    ) {
        CreateProjectResponse createProjectResponse = projectService.createProject(createProjectRequest, memberId);
        return new ResponseEntity<>(createProjectResponse, CREATED);
    }

    @GetMapping
    public ResponseEntity<GetProjectMembersResponse> getProjects(
            @SessionAttribute(name = "memberId") Long memberId
    ) {
        GetProjectMembersResponse getProjectMembersResponse = projectService.getProjects(memberId);
        return ResponseEntity.ok(getProjectMembersResponse);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<GetMembersInProjectResponse>> getMembersInProject(
            @PathVariable("projectId") Long projectId
    ) {
        return ResponseEntity.ok(projectService.getMembers(projectId));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @Validated
            @RequestBody
            UpdateProjectRequest updateProjectRequest,
            @PathVariable(name = "projectId") Long projectId,
            @SessionAttribute(name = "memberId") Long memberId
    ) {
        projectService.updateProject(projectId, updateProjectRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<Void> inviteProject(
            @Validated
            @RequestBody
            InviteProjectRequest inviteProjectRequest,
            @PathVariable(name = "projectId") Long projectId
    ) {
        projectService.inviteProject(projectId, inviteProjectRequest);
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/{projectId}/leave")
    public ResponseEntity<Void> leaveProject(
            @PathVariable(name = "projectId") Long projectId,
            @SessionAttribute Long memberId
    ) {
        log.info("controller start");
        projectService.leaveProject(projectId, memberId);
        log.info("controller end");
        return ResponseEntity.ok().build();
    }
}
