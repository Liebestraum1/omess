package org.sixback.omess.kanbanboard;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.sixback.omess.domain.kanbanboard.repository.IssueRepository;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.repository.LabelRepository;
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
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
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

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    LabelRepository labelRepository;

    @BeforeEach
    public void initTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);
    }

    // 칸반보드 생성/삭제 테스트
    @Test
    public void creatKanbanBoard() {
        Member member = memberRepository.findById(1L).get();
        Project project = projectRepository.findById(1L).get();

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
            kanbanBoardService.deleteKanbanBoard(member.getId(), findKanban.getId(), project.getId());
        }

        List<Module> afterModules = moduleRepository.findAll();

        System.out.println("삭제 후 " + afterModules.size());
    }

    @Test
    public void getKanbanBoard() {
        Member member = memberRepository.findById(1L).get();
        Project project = projectRepository.findById(1L).get();

        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드 2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);

        List<Issue> issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            issues.add(new Issue("할일 " + i, "이거해라", 0, 0, kanbanBoard, null, null));
        }

        for (int i = 0; i < 10; i++) {
            issues.add(new Issue("할일 " + i, "이거해라", 0, 0, kanbanBoard2, null, null));
        }

        issueRepository.saveAll(issues);

        List<Label> labels = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                Label label = new Label("라벨 " + i);
                labels.add(label);
                labelRepository.save(label);

                issues.get(i).updateLabel(label);
            }
        }

        em.flush();

        GetKanbanBoardResponse getKanbanBoardResponse = kanbanBoardService.getKanbanBoard(member.getId(), kanbanBoard.getId(), project.getId());
        System.out.println(getKanbanBoardResponse.title());
        System.out.println(getKanbanBoardResponse.issues().size());

        for (int i = 0; i < getKanbanBoardResponse.issues().size(); i++) {
            if (getKanbanBoardResponse.issues().get(i).label() != null) {
                System.out.println("이슈 : " + getKanbanBoardResponse.issues().get(i).title() + " 라벨 : " + getKanbanBoardResponse.issues().get(i).label().name());
            }else{
                System.out.println("이슈 : " + getKanbanBoardResponse.issues().get(i).title() + " 라벨 : " + "없음");
            }
        }
    }

}
