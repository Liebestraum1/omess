package org.sixback.omess.domain.project.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String name;

    public Project(String name) {
        this.name = name;
    }

    public Project(Long id, String name) {
        this(name);
        this.name = name;
    }

    public void updateProject(String name) {
        this.name = name;
    }
}
