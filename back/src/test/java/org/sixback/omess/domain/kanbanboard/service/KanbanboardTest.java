package org.sixback.omess.domain.kanbanboard.service;

import com.mysema.commons.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.dto.request.DeleteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.WriteIssueRequest;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    // 칸반보드 생성/삭제 테스트
    @Test
    public void creatKanbanBoardTest() {
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
            kanbanBoardService.deleteKanbanBoard(member.getId(), findKanban.getId(), project.getId());
        }

        List<Module> afterModules = moduleRepository.findAll();

        System.out.println("삭제 후 " + afterModules.size());
    }

    @Test
    public void getKanbanBoardTest() {
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

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
                Label label = new Label("라벨 " + i, kanbanBoard);
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

    @Test
    public void createIssueTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");
        Member member2 = new Member("su", "wjs3436", "wb3436");

        memberRepository.save(member);
        memberRepository.save(member2);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);
        ProjectMember projectMember2 = new ProjectMember(project, member2);

        projectMemberRepository.save(projectMember);
        projectMemberRepository.save(projectMember2);

        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드 2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);

        WriteIssueRequest writeIssueRequest = new WriteIssueRequest();
        writeIssueRequest.setTitle("이슈생성 테스트");
        writeIssueRequest.setContent("이슈생성 테스트 내용");

        kanbanBoardService.createIssue(project.getId(), kanbanBoard.getId(), writeIssueRequest);

        Issue findIssue = issueRepository.findByTitle("이슈생성 테스트");

        assertThat(findIssue.getContent()).isEqualTo("이슈생성 테스트 내용");
    }

    @Test
    public void deleteIssueTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");
        Member member2 = new Member("su", "wjs3436", "wb3436");

        memberRepository.save(member);
        memberRepository.save(member2);


        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);
        ProjectMember projectMember2 = new ProjectMember(project, member2);

        projectMemberRepository.save(projectMember);
        projectMemberRepository.save(projectMember2);

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
                Label label = new Label("라벨 " + i, kanbanBoard);
                labels.add(label);
                labelRepository.save(label);

                issues.get(i).updateLabel(label);
                issues.get(i).updateMember(member2);
            }else{
                issues.get(i).updateMember(member);
            }
        }

        em.flush();

        kanbanBoardService.deleteIssue(member2.getId(), project.getId(), kanbanBoard.getId(), issues.get(0).getId());

        List<Issue> allIssue = issueRepository.findAll();
        List<Label> allLabel = labelRepository.findAll();

        assertThat(allIssue.size() ).isEqualTo(19);
        assertThat(allLabel.size()).isEqualTo(10);
    }

}