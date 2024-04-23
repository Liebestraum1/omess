package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.project.model.entity.Project;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ApiSpecification extends Module {
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "api_specification_id")
    private List<Domain> domains = new ArrayList<>();

    @Builder
    public ApiSpecification(String moduleName, String moduleCategory, Project project) {
        super(moduleName, moduleCategory, project);
    }
}
