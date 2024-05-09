package com.sixback.omesschat.domain.file.repository;

import com.sixback.omesschat.domain.file.model.entity.FileInformation;
import com.sixback.omesschat.domain.file.model.entity.ReferenceType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileInformationRepository extends ReactiveCrudRepository<FileInformation, Long> {

    @Query("SELECT id, original_name, path " +
            "FROM file_information " +
            "WHERE reference_id = :id AND reference_type = :type")
    Flux<FileInformation> findByReferenceIdAndType(String id, ReferenceType type);
}
