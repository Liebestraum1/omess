package org.sixback.omess.domain.apispecification.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.domain.apispecification.model.dto.CreateApiSpecificationRequest;
import org.sixback.omess.domain.apispecification.repository.ApiSpecificationRepository;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("API 명세서 생성 성공 테스트")
    void createApiSpecificationSuccessTest(){
        //given
        Project project = newProject();
        Member member = TestUtils.makeMember();
        CreateApiSpecificationRequest request = new CreateApiSpecificationRequest(
                "testName", "testCategory"
        );

        when(projectMemberRepository.findByProjectIdAndMemberId(project.getId(), member.getId()))
                .thenReturn(Optional.of(new ProjectMember(1L, project, member)));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        //when

        //then
        Assertions.assertDoesNotThrow(
                () -> apiSpecificationService.createApiSpecification(member.getId(), project.getId(), request)
        );
    }

    @Test
    @DisplayName("API 명세서 생성 실패 테스트")
    void createApiSpecificationFailTest(){
        //given
        Project project = newProject();
        Member member = TestUtils.makeMember();
        CreateApiSpecificationRequest request = new CreateApiSpecificationRequest(
                "testName", "testCategory"
        );

        when(projectMemberRepository.findByProjectIdAndMemberId(project.getId(), member.getId()))
                .thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> apiSpecificationService.createApiSpecification(member.getId(), project.getId(), request)
        );
    }

}