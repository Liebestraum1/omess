package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PathVariable extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 20)
    private String path;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 50)
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "api_id")
    Api api;
}
