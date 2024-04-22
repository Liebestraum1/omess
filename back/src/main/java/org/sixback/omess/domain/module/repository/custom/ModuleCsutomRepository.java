package org.sixback.omess.domain.module.repository.custom;

import org.sixback.omess.domain.module.model.entity.Module;

import java.util.List;

public interface ModuleCsutomRepository  {
    List<Module> findByProjectID(Long projectId);
}
