package org.sixback.omess.domain.kanbanboard.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.WriteIssueRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.request.kanbanboard.WriteKanbanBoardRequest;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueDetailResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.issue.GetIssueResponse;
import org.sixback.omess.domain.kanbanboard.model.dto.response.kanbanboard.GetKanbanBoardResponse;
import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.model.entity.KanbanBoard;
import org.sixback.omess.domain.kanbanboard.model.entity.Label;
import org.sixback.omess.domain.kanbanboard.repository.IssueRepository;
import org.sixback.omess.domain.kanbanboard.repository.KanbanBoardRepository;
import org.sixback.omess.domain.kanbanboard.repository.LabelRepository;
import org.sixback.omess.domain.member.model.dto.response.GetIssueResponses;
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
public class KanbanboardServiceTest {
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
        String k1Path = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(k1Path);

        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드 2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);
        String k2Path = "P" + project.getId() + "/K" + kanbanBoard2.getId();
        kanbanBoard2.updatePath(k2Path);

        List<Issue> issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            issues.add(new Issue("할일 " + i, "이거해라", 1, 1, kanbanBoard, null, null));
        }

        for (int i = 0; i < 10; i++) {
            issues.add(new Issue("할일 " + i, "이거해라", 1, 1, kanbanBoard2, null, null));
        }

        issueRepository.saveAll(issues);

        List<Label> labels = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                Label label = new Label("라벨 " + i, kanbanBoard);
                labels.add(label);
                labelRepository.save(label);

                String lPath = k1Path + "/L" + label.getId();
                label.updatePath(lPath);

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
        writeIssueRequest.setStatus(1);
        writeIssueRequest.setImportance(1);

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
        String k1Path = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(k1Path);

        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드 2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);
        String k2Path = "P" + project.getId() + "/K" + kanbanBoard2.getId();
        kanbanBoard.updatePath(k2Path);

        List<Issue> issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Issue issue = new Issue("할일 " + i, "이거해라", 1, 1, kanbanBoard, null, null);
            issueRepository.save(issue);
            String iPath = k1Path + "/I" + issue.getId();
            issue.updatePath(iPath);

            issues.add(issue);
        }

        for (int i = 0; i < 10; i++) {
            Issue issue = new Issue("할일 " + i, "이거해라", 1, 1, kanbanBoard2, null, null);
            issueRepository.save(issue);
            String iPath = k2Path + "/I" + issue.getId();
            issue.updatePath(iPath);

            issues.add(issue);
        }

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

    @Test
    public void updateIssueTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);

        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        String kPath = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(kPath);
        
        Issue issue = new Issue("이슈 테스트", "이슈 테스트 입니다.", 1, 1, kanbanBoard, null, null);
        
        issueRepository.save(issue);

        String iPath = kPath + "/I" + issue.getId();
        issue.updatePath(iPath);

        UpdateIssueRequest updateIssueRequest = new UpdateIssueRequest();
        
        updateIssueRequest.setTitle("수정됨");
        updateIssueRequest.setContent("수정됨요");
        
        kanbanBoardService.updateIssue(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), updateIssueRequest);

        em.flush();

        Issue findIssue = issueRepository.findById(issue.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertThat(findIssue.getContent()).isEqualTo(updateIssueRequest.getContent());

    }

    @Test
    public void updateIssueChargerTest(){
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);

        Project project = new Project("프젝 1");

        projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        String kPath = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(kPath);

        Issue issue = new Issue("이슈 테스트", "이슈 테스트 입니다.", 1, 1, kanbanBoard, null, null);

        issueRepository.save(issue);

        String iPath = kPath + "/I" + issue.getId();
        issue.updatePath(iPath);

        // 담당자 수정

        kanbanBoardService.updateIssueMember(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), member.getId());

        em.flush();

        Issue findIssue = issueRepository.findById(issue.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertThat(findIssue.getCharger()).isEqualTo(member);

        // 라벨 수정

        Label label = new Label("라벨 ", kanbanBoard);
        labelRepository.save(label);

        kanbanBoardService.updateIssueLabel(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), label.getId());

        em.flush();

        Issue findIssueLabel = issueRepository.findById(issue.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertThat(findIssueLabel.getLabel()).isEqualTo(label);

        // 중요도 수정

        kanbanBoardService.updateIssueImportance(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), 1);

        em.flush();

        Issue findIssueImportance = issueRepository.findById(issue.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertThat(findIssueImportance.getImportance()).isEqualTo(1);

        // 상태 수정

        kanbanBoardService.updateIssueStatus(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), 3);

        em.flush();

        Issue findIssueStatus = issueRepository.findById(issue.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertThat(findIssueStatus.getStatus()).isEqualTo(3);
    }

    @Test
    public void getIssues(){
        // 멤버 생성
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);
        Member member2 = new Member("su", "su", "su");

        memberRepository.save(member2);

        Member[] members = {member, member2};

        // 프로젝트 생성
        Project project = new Project("프젝 1");

        projectRepository.save(project);

        // 프로젝트 멤버 생성
        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        // 칸반 보드 생성
        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        String kPath = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(kPath);

        // 칸반 보드 생성
        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);

        String kPath2 = "P" + project.getId() + "/K" + kanbanBoard2.getId();
        kanbanBoard.updatePath(kPath2);

        // 이슈 생성
        Issue issue = new Issue("이슈 테스트", "이슈 테스트 입니다.", 1, 1, kanbanBoard, null, null);

        issueRepository.save(issue);

        String iPath = kPath + "/I" + issue.getId();
        issue.updatePath(iPath);

        // 담당자 수정
        kanbanBoardService.updateIssueMember(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), member.getId());

        // 라벨 수정
        Label label = new Label("라벨", kanbanBoard);
        labelRepository.save(label);

        // 라벨 수정
        Label label2 = new Label("라벨2", kanbanBoard);
        labelRepository.save(label2);

        Label[] labels = {label, label2};

        kanbanBoardService.updateIssueLabel(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), label.getId());

        for (int i = 0; i < 100; i++) {
            if(i % 2 == 0){
                // 이슈 생성
                Issue issues = new Issue("이슈 테스트" + i, "이슈 테스트 입니다.", (int)(Math.random() * 3) + 1, (int)(Math.random() * 3) + 1, kanbanBoard, null, null);

                issueRepository.save(issues);

                String iPath2 = kPath + "/I" + issue.getId();
                issues.updatePath(iPath2);

                int randomValue = (Math.random() < 0.5) ? 0 : 1;
                issues.updateMember(members[randomValue]);

                int randomValue2 = (Math.random() < 0.5) ? 0 : 1;
                issues.updateLabel(labels[randomValue2]);
            }else{
                // 이슈 생성
                Issue issues = new Issue("이슈 테스트" + i, "이슈 테스트 입니다.", (int)(Math.random() * 3) + 1, (int)(Math.random() * 3) + 1, kanbanBoard2, null, null);

                issueRepository.save(issues);

                String iPath2 = kPath + "/I" + issue.getId();
                issues.updatePath(iPath2);
            }

        }

        // 이슈 조회
        GetIssueResponses getIssueResponses = kanbanBoardService.getIssues(member.getId(), project.getId(), kanbanBoard.getId(), member.getId(), label.getId(), 1);

        for (int i = 0; i < getIssueResponses.issues().size(); i++) {
            GetIssueResponse getIssueResponse = getIssueResponses.issues().get(i);

            assertThat(getIssueResponse.charger().id()).isEqualTo(member.getId());
            assertThat(getIssueResponse.label().labelId()).isEqualTo(label.getId());
            assertThat(getIssueResponse.importance()).isEqualTo(1);
        }
    }

    @Test
    public void getIssueTest(){
        // 멤버 생성
        Member member = new Member("bam", "wjs6265", "wb6265");

        memberRepository.save(member);
        Member member2 = new Member("su", "su", "su");

        memberRepository.save(member2);

        Member[] members = {member, member2};

        // 프로젝트 생성
        Project project = new Project("프젝 1");

        projectRepository.save(project);

        // 프로젝트 멤버 생성
        ProjectMember projectMember = new ProjectMember(project, member);

        projectMemberRepository.save(projectMember);

        // 칸반 보드 생성
        KanbanBoard kanbanBoard = new KanbanBoard("칸반보드", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard);

        String kPath = "P" + project.getId() + "/K" + kanbanBoard.getId();
        kanbanBoard.updatePath(kPath);

        // 칸반 보드 생성
        KanbanBoard kanbanBoard2 = new KanbanBoard("칸반보드2", "KanbanBoard", project);
        kanbanBoardRepository.save(kanbanBoard2);

        String kPath2 = "P" + project.getId() + "/K" + kanbanBoard2.getId();
        kanbanBoard.updatePath(kPath2);

        // 이슈 생성
        Issue issue = new Issue("이슈 테스트", "이슈 테스트 입니다.", 1, 1, kanbanBoard, null, null);

        issueRepository.save(issue);

        String iPath = kPath + "/I" + issue.getId();
        issue.updatePath(iPath);

        // 담당자 수정
        kanbanBoardService.updateIssueMember(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), member.getId());

        // 라벨 수정
        Label label = new Label("라벨", kanbanBoard);
        labelRepository.save(label);

        // 라벨 수정
        Label label2 = new Label("라벨2", kanbanBoard);
        labelRepository.save(label2);

        Label[] labels = {label, label2};

        kanbanBoardService.updateIssueLabel(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId(), label.getId());

        for (int i = 0; i < 100; i++) {
            if(i % 2 == 0){
                // 이슈 생성
                Issue issues = new Issue("이슈 테스트" + i, "이슈 테스트 입니다.", (int)(Math.random() * 3) + 1, (int)(Math.random() * 3) + 1, kanbanBoard, null, null);

                issueRepository.save(issues);

                String iPath2 = kPath + "/I" + issue.getId();
                issues.updatePath(iPath2);

                int randomValue = (Math.random() < 0.5) ? 0 : 1;
                issues.updateMember(members[randomValue]);

                int randomValue2 = (Math.random() < 0.5) ? 0 : 1;
                issues.updateLabel(labels[randomValue2]);
            }else{
                // 이슈 생성
                Issue issues = new Issue("이슈 테스트" + i, "이슈 테스트 입니다.", (int)(Math.random() * 3) + 1, (int)(Math.random() * 3) + 1, kanbanBoard2, null, null);

                issueRepository.save(issues);

                String iPath2 = kPath + "/I" + issue.getId();
                issues.updatePath(iPath2);
            }

        }

        GetIssueDetailResponse getIssueDetailResponse = kanbanBoardService.getIssue(member.getId(), project.getId(), kanbanBoard.getId(), issue.getId());

        assertThat(getIssueDetailResponse.issueId()).isEqualTo(issue.getId());
    }
}
