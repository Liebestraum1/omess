package org.sixback.omess.domain.module.repository.custom;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.model.entity.Module;
import org.sixback.omess.domain.module.model.entity.QModule;

import java.util.List;

@RequiredArgsConstructor
public class ModuleRepositoryImpl implements ModuleCsutomRepository{
    private final JPQLQueryFactory jpaQueryFactory;
    private final QModule qModule = QModule.module;
    @Override
    public List<Module> findByProjectID(Long projectId) {
        return jpaQueryFactory
                .select(qModule)
                .from(qModule)
                .where(qModule.project.id.eq(projectId))
                .fetch();
    }
}
