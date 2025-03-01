package org.sixback.omess.domain.module.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.module.model.dto.request.UpdateMouleRequest;
import org.sixback.omess.domain.module.model.dto.response.GetModuleResponse;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.sixback.omess.common.TestUtils.makeProjectMember;

@SpringBootTest
@Transactional
public class ModuleTest {
    @Autowired
    KanbanBoardRepository kanbanBoardRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    KanbanBoardService kanbanBoardService;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ModuleService moduleService;

    // 모듈 조회 테스트
    @Test
    public void getModules() {
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = makeProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        for (int i = 0; i < 10; i++) {
            WriteKanbanBoardRequest writeKanbanBoardRequest = new WriteKanbanBoardRequest();
            writeKanbanBoardRequest.setName("칸반보드");
            writeKanbanBoardRequest.setCategory("kanbanboards");

            kanbanBoardService.createKanbanBoard(member.getId(), project.getId(), writeKanbanBoardRequest);
        }

        List<GetModuleResponse> getModuleResponses = moduleService.getModules(project.getId(), member.getId());

        for (int i = 0; i < getModuleResponses.size(); i++) {
            System.out.println(getModuleResponses.get(i).title());
        }

    }

    // 모듈 수정 테스트
    @Test
    public void updateModule() {
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);

        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = makeProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        KanbanBoard kanbanBoard = new KanbanBoard("api 명세서", "KanbanBoard", project);

        kanbanBoardRepository.save(kanbanBoard);

        Optional<Module> findModule = moduleRepository.findById(kanbanBoard.getId());

        if (findModule.isPresent()) {
            Module module = findModule.get();

            UpdateMouleRequest updateMouleRequest = new UpdateMouleRequest();
            updateMouleRequest.setTitle("칸반보드");

            moduleService.updateModule(member.getId(), module.getId(), project.getId(), updateMouleRequest);

            Optional<Module> updateModule = moduleRepository.findById(kanbanBoard.getId());

            System.out.println(updateModule.get().getTitle());
        }

    }
}
