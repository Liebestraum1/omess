package org.sixback.omess.domain.project.mapper;

import org.sixback.omess.domain.project.model.dto.request.CreateProjectRequest;
import org.sixback.omess.domain.project.model.dto.response.CreateProjectResponse;
import org.sixback.omess.domain.project.model.entity.Project;

import static org.sixback.omess.domain.project.model.enums.ProjectRole.OWNER;


public class ProjectMapper {
    public static Project toProject(CreateProjectRequest createProjectRequest) {
        return new Project(createProjectRequest.getName());
    }

    public static CreateProjectResponse toCreateProjectResponse(Project project) {
        return new CreateProjectResponse(project.getId(), project.getName(), OWNER);
    }
}
