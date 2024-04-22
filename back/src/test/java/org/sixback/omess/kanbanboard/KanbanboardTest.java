package org.sixback.omess.kanbanboard;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.service.KanbanBoardService;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.sixback.omess.domain.project.repository.ProjectMemberRepository;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class KanbanboardTest {
    @Autowired
    EntityManager em;

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

    // 칸반보드 생성/삭제 테스트
    @Test
    public void creatKanbanBoard() {
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        WriteKanbanBoardRequest writeKanbanBoardRequest = new WriteKanbanBoardRequest();
        writeKanbanBoardRequest.setTitle("칸반보드");

        kanbanBoardService.createKanbanBoard(member.getId(), project.getId(), writeKanbanBoardRequest);

        List<KanbanBoard> kanbanBoards = kanbanBoardRepository.findAll();

        List<Module> boforeModules = moduleRepository.findAll();

        System.out.println("삭제 전 " + boforeModules.size());

        for (int i = 0; i < kanbanBoards.size(); i++) {
            KanbanBoard findKanban = kanbanBoards.get(i);
            System.out.println(findKanban.getTitle());
            DeleteKanbanBoardRequest deleteKanbanBoardRequest = new DeleteKanbanBoardRequest();
            deleteKanbanBoardRequest.setProjectId(project.getId());
            kanbanBoardService.deleteKanbanBoard(member.getId(), findKanban.getId(), deleteKanbanBoardRequest);
        }

        List<Module> afterModules = moduleRepository.findAll();

        System.out.println("삭제 후 " + afterModules.size());
    }

}
