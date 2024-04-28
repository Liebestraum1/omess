package org.sixback.omess.domain.apispecification.repository;

import org.sixback.omess.domain.apispecification.model.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Api, Long> {
}
