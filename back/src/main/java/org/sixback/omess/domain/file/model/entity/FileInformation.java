package org.sixback.omess.domain.file.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.domain.file.model.enums.ReferenceType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FileInformation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String originalName;

    @Column(length = 1000, nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    private Long referenceId;

    @Enumerated(STRING)
    private ReferenceType referenceType;

    @Column(nullable = false)
    private boolean isSaved = false;

    @Builder
    public FileInformation(
            Long id, String name, String originalName,
            String address, String fileExtension, Long referenceId,
            ReferenceType referenceType, boolean isSaved
    ) {
        this.id = id;
        this.name = name;
        this.originalName = originalName;
        this.address = address;
        this.fileExtension = fileExtension;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.isSaved = isSaved;
    }

    public void changeSaveStatus(boolean isSaved) {
        this.isSaved = isSaved;
    }
}
