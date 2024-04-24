package org.sixback.omess.domain.module.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.model.entity.QModule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModuleRepositoryImpl implements ModuleCsutomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final QModule qModule = QModule.module;
    @Override
    public List<Module> findByProjectId(Long projectId) {
        return jpaQueryFactory
                .select(qModule)
                .from(qModule)
                .where(qModule.project.id.eq(projectId))
                .fetch();
    }
}
