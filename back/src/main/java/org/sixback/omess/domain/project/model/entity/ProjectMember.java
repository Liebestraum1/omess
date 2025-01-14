package org.sixback.omess.domain.project.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.project.model.enums.ProjectRole;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProjectMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    public ProjectMember(Project project, Member member, ProjectRole projectRole) {
        this.project = project;
        this.member = member;
        this.projectRole = projectRole;
    }

    public ProjectMember(Long id, Project project, Member member, ProjectRole projectRole) {
        this(project, member, projectRole);
        this.id = id;
    }
}
