package org.sixback.omess.domain.kanbanboard.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.project.model.entity.Project;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kanbanboard")
public class KanbanBoard extends Module {
    @Column(length = 20)
    String path;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "kanbanboard_id")
    List<Issue> issues;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "kanbanboard_id")
    List<Label> labels;

    public KanbanBoard(String title, String category, Project project) {
        super(title, category, project);
    }

    public void updatePath(String path) {
        if (this.path == null) {
            this.path = path;
        }
    }
}
