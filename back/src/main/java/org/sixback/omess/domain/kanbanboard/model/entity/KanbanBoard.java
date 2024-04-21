package org.sixback.omess.domain.kanbanboard.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sixback.omess.common.BaseTimeEntity;
import org.sixback.omess.domain.module.model.entity.Module;

import java.util.List;

@Entity
@Getter
@Setter //삭제
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 수정
public class KanbanBoard extends Module {

    @OneToMany(fetch = FetchType.LAZY)
    List<Issue> issues;

    @OneToMany(fetch = FetchType.LAZY)
    List<Label> labels;
}
