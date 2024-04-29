package org.sixback.omess.domain.apispecification.repository;

import java.util.Optional;

import org.sixback.omess.domain.apispecification.model.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Api, Long> {
	boolean existsByPath(String path);

	void deleteByPath(String path);

	Optional<Api> findByPath(String path);
}
