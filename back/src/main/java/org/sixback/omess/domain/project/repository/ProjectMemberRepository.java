package org.sixback.omess.domain.project.repository;

import org.sixback.omess.domain.project.model.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query(value = "select pm from ProjectMember pm where pm.project.id = :projectId and pm.member.id = :memberId")
    Optional<ProjectMember> findByProjectIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
}
