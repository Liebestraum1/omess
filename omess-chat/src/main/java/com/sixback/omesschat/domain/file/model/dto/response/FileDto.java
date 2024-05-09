package com.sixback.omesschat.domain.file.model.dto.response;

import lombok.Getter;

@Getter
public class FileDto {
    private String filename;
    private String url;

    protected FileDto() {}
    public FileDto(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }
}
