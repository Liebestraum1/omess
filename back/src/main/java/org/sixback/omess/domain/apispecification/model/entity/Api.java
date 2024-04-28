package org.sixback.omess.domain.apispecification.model.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

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
    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String requestSchema;

    @Column(columnDefinition = "TEXT")
    private String responseSchema;

    @Column(nullable = false)
    Short statusCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = ALL)
    List<RequestHeader> requestHeaders = new ArrayList<>();

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = ALL)
    List<QueryParam> queryParams = new ArrayList<>();

    @OneToMany(mappedBy = "api", fetch = LAZY, cascade = ALL)
    List<PathVariable> pathVariables = new ArrayList<>();

    @Builder
    public Api(String method, String name, String description, String endpoint, String requestSchema,
        String responseSchema, Short statusCode, Domain domain) {
        this.method = method;
        this.name = name;
        this.description = description;
        this.endpoint = endpoint;
        this.requestSchema = requestSchema;
        this.responseSchema = responseSchema;
        this.statusCode = statusCode;
        this.domain = domain;
    }

    public void addPath(String path) {
        this.path = path;
    }
}
