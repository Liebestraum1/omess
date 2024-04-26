package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Domain extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 20)
    private String path;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "api_specification_id")
    ApiSpecification apiSpecification;

    @OneToMany(mappedBy = "domain", fetch = LAZY, cascade = REMOVE)
    List<Api> apis = new ArrayList<>();

    @Builder
    public Domain(String name, ApiSpecification apiSpecification) {
        this.name = name;
        this.apiSpecification = apiSpecification;
    }

    public void addPath(String path){
        this.path = path;
    }
}
