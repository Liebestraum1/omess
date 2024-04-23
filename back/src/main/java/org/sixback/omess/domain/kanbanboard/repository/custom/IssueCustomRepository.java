package org.sixback.omess.domain.kanbanboard.repository.custom;

import org.sixback.omess.domain.kanbanboard.model.entity.Issue;

import java.util.List;

public interface IssueCustomRepository {
    List<Issue> findByModuleId(Long moduleId);
}
