package org.sixback.omess.domain.apispecification.model.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.project.model.entity.Project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ApiSpecification extends Module {
    @Column(length = 20)
    private String path;

    @OneToMany(mappedBy = "apiSpecification", fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<Domain> domains = new ArrayList<>();

    @Builder
    public ApiSpecification(String moduleName, String moduleCategory, Project project) {
        super(moduleName, moduleCategory, project);
    }

    public void addPath(String path) {
        this.path = path;
    }
}
