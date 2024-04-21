package org.sixback.omess.kanbanboard;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.member.repository.MemberRepository;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    public void creatKanbanBoard(){
        Member member = new Member();

        member.setEmail("wjs6265");
        member.setPassword("wb6265");

        memberRepository.save(member);

        Project project = new Project();
        project.setTitle("프젝 1");

        projectRepository.save(project);

        KanbanBoard module = new KanbanBoard();
        module.setTitle("칸반보드");
        module.setProject(project);
        kanbanBoardRepository.save(module);

        Long id = 1L;

        Module module1 = moduleRepository.findById(id).get();

        System.out.println(module1.getTitle());
    }

}
