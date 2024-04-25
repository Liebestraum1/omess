package org.sixback.omess.domain.kanbanboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class KanbanBoardRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    KanbanBoardRepository kanbanBoardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;
    @Autowired
    ModuleRepository moduleRepository;

    @Test
    public void deleteKanbanBoardTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        String path = "P" + project.getId() + "/K";

        KanbanBoard kanbanBoard = new KanbanBoard("칸테", "칸반", project);

        kanbanBoardRepository.save(kanbanBoard);

        kanbanBoardRepository.delete(kanbanBoard);

        List<Module> allModule = moduleRepository.findAll();

        assertThat(allModule.size()).isEqualTo(0);
    }
}
