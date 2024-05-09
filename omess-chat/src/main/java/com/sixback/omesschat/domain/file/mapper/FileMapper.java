package com.sixback.omesschat.domain.file.mapper;

import com.sixback.omesschat.domain.file.model.dto.response.FileDto;
import com.sixback.omesschat.domain.file.model.entity.FileInformation;

public class FileMapper {

    private static final String URL = "http://k10a301.p.ssafy.io:4380/";

    public static FileDto toFileDto(FileInformation fileInformation) {
        return new FileDto(fileInformation.getOriginalName(), URL + fileInformation.getPath());
    }
}
