package org.sixback.omess.domain.kanbanboard.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.kanbanboard.model.dto.request.issue.UpdateIssueRequest;
import org.sixback.omess.domain.member.model.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 90, nullable = false)
    private String title;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(nullable = true)
    @ColumnDefault("0")
    private Integer importance;

    @Column(nullable = true)
    @ColumnDefault("0")
    private Integer status;

    @Column(length = 20)
    String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanbanboard_id")
    private KanbanBoard kanbanBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_id")
    private Member charger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;


    public Issue(String title, String content, Integer importance, Integer status, KanbanBoard kanbanBoard, Member charger, Label label) {
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.status = status;
        this.kanbanBoard = kanbanBoard;
        this.charger = charger;
        this.label = label;
    }

    public void updateIssue(UpdateIssueRequest updateIssueRequest){
        this.title = updateIssueRequest.getTitle();
        this.content = updateIssueRequest.getContent();
    }

    public void updateLabel(Label label){
        this.label = label;
    }

    public void updateMember(Member charger) {
        this.charger = charger;
    }

    public void updatePath(String path){
        if(this.path == null){
            this.path = path;
        }
    }

    public void updateImportance(Integer importance) {
        this.importance = importance;
    }

    public void updateStatus(Integer status) {
        this.status = status;
    }
}
