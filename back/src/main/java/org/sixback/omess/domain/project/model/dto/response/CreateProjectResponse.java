package org.sixback.omess.domain.project.model.dto.response;

import org.sixback.omess.domain.project.model.enums.ProjectRole;

import static org.sixback.omess.domain.project.model.enums.ProjectRole.OWNER;

public record CreateProjectResponse(
        Long projectId,
        String name,
        ProjectRole projectRole
) {
    public CreateProjectResponse(Long projectId, String name) {
        this(projectId, name, OWNER);
    }
}
