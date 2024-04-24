package org.sixback.omess.domain.kanbanboard.repository;

import org.sixback.omess.domain.kanbanboard.model.entity.Issue;
import org.sixback.omess.domain.kanbanboard.repository.custom.IssueCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long>, IssueCustomRepository {
    Issue findByTitle(String title);
}
