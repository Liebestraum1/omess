package org.sixback.omess.domain.apispecification.model.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.sixback.omess.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class QueryParam extends BaseTimeEntity {
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

    public QueryParam(String name, String description, Api api) {
        this.name = name;
        this.description = description;
        this.api = api;
    }
}
