package org.sixback.omess.domain.apispecification.repository;

import java.util.Optional;

import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    Optional<Domain> findByPath(String path);

	void deleteByPath(String path);

	boolean existsByPath(String path);
}
