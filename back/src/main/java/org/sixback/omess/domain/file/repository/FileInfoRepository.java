package org.sixback.omess.domain.file.repository;


import org.sixback.omess.domain.file.model.entity.FileInformation;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInformation, Long> {
    List<FileInformation> findALlByReferenceTypeAndReferenceId(ReferenceType referenceType, String referenceId);

    List<FileInformation> findAllByReferenceTypeAndReferenceIdIn(ReferenceType referenceType, List<Long> referenceIds);
}
