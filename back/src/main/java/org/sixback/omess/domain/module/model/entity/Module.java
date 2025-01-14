package org.sixback.omess.domain.module.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.project.model.entity.Project;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Module extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public Module(String title, String category, Project project){
        this.title = title;
        this.category = category;
        this.project = project;
    }

    public void updateModule(String title){
        this.title = title;
    }
}
