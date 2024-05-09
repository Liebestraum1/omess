package com.sixback.omesschat.domain.file.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInformation {

    @Id
    private Long id;
    private String originalName;
    private String path;
}
