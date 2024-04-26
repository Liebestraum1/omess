package org.sixback.omess.domain.apispecification.model.entity;

import jakarta.persistence.*;
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
public class Api extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 20)
    private String path;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false, length = 2000)
    private String endPoint;

    @Column(nullable = false)
    Short status_code;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = REMOVE)
    List<RequestHeader> requestHeaders = new ArrayList<>();

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = REMOVE)
    List<QueryParam> queryParams = new ArrayList<>();

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = REMOVE)
    List<PathVariable> pathVariables = new ArrayList<>();

}
