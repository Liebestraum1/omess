package org.sixback.omess.domain.module.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.project.model.entity.Project;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Module extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 50, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public Module(String title, Project project){
        this.title = title;
        this.project = project;
    }
}
