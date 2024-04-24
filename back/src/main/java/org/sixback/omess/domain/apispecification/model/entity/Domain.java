package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Domain extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "api_specification_id")
    ApiSpecification apiSpecification;

    public Domain(String name, ApiSpecification apiSpecification) {
        this.name = name;
        this.apiSpecification = apiSpecification;
    }
}
