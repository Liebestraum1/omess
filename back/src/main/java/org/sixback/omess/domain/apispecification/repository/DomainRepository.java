package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    Optional<Domain> findByPath(String path);
}
