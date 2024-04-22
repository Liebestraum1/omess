package org.sixback.omess.domain.module.repository;

import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.repository.custom.ModuleCsutomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long>, ModuleCsutomRepository {
}
