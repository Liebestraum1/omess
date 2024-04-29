package org.sixback.omess.domain.apispecification.model.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.sixback.omess.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "domain", fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
    @BatchSize(size = 20)
    List<Api> apis = new ArrayList<>();

    @Builder
    public Domain(String name, ApiSpecification apiSpecification) {
        this.name = name;
        this.apiSpecification = apiSpecification;
    }

    public void addPath(String path){
        this.path = path;
    }

    public void updateName(String name){
        this.name = name;
    }
}
