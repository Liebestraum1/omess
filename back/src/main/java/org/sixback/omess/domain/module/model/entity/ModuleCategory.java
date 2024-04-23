package org.sixback.omess.domain.module.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModuleCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String category;

    @Column(length = 20, nullable = false)
    private String path;

    public ModuleCategory(String category, String path){
        this.category = category;
        this.path = path;
    }

    public void updateModuleCategory(String category, String path){
        this.category = category;
        this.path = path;
    }
}