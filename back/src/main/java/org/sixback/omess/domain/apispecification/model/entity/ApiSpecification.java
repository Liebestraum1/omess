package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.project.model.entity.Project;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ApiSpecification extends Module {
    @Column(length = 20)
    private String path;

    @OneToMany(mappedBy = "apiSpecification", fetch = LAZY, cascade = REMOVE)
    private List<Domain> domains = new ArrayList<>();

    @Builder
    public ApiSpecification(String moduleName, String moduleCategory, Project project) {
        super(moduleName, moduleCategory, project);
    }
}
