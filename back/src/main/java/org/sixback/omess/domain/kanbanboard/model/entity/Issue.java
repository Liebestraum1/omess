package org.sixback.omess.domain.kanbanboard.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.domain.member.model.entity.Member;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 90, nullable = false)
    private String title;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(nullable = true)
    private int importance;

    @Column(nullable = true)
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanbanboard_id")
    private KanbanBoard kanbanBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;

    public Issue(String title, String content, int importance, int status, KanbanBoard kanbanBoard, Member member, Label label) {
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.status = status;
        this.kanbanBoard = kanbanBoard;
        this.member = member;
        this.label = label;
    }

    public void updateLabel(Label label){
        this.label = label;
    }
}
