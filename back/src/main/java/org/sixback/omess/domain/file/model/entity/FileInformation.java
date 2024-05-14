package org.sixback.omess.domain.file.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.sixback.omess.domain.file.model.enums.ReferenceType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = PROTECTED)
public class FileInformation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String originalName;

    @Column(length = 1000, nullable = false)
    private String path;

    @Column(length = 50, nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String referenceId;

    @Enumerated(STRING)
    private ReferenceType referenceType;

    @Column(nullable = false)
    private boolean isSaved = false;

    @Builder
    public FileInformation(
            Long id, String originalName,
            String path, String contentType, String referenceId,
            ReferenceType referenceType, boolean isSaved
    ) {
        this.id = id;
        this.originalName = originalName;
        this.path = path;
        this.contentType = contentType;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.isSaved = isSaved;
    }

    public void changeSaveStatus(boolean isSaved) {
        this.isSaved = isSaved;
    }
}
