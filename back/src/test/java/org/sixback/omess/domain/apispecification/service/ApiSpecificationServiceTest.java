package org.sixback.omess.domain.apispecification.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ApiSpecificationServiceTest {
    @InjectMocks
    private ApiSpecificationService apiSpecificationService;

    @Mock
    private ApiSpecificationRepository apiSpecificationRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    private Project newProject(){
        return new Project(1L, "testProject");
    }

}