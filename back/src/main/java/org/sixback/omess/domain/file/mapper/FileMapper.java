package org.sixback.omess.domain.file.mapper;

import org.sixback.omess.domain.file.model.dto.response.GetFileInfoResponse;
import org.sixback.omess.domain.file.model.dto.response.UploadFileResponse;
import org.sixback.omess.domain.file.model.entity.FileInformation;

public class FileMapper {
    public static GetFileInfoResponse toGetFileInfoResponse(FileInformation fileInformation, String address) {
        return new GetFileInfoResponse(
                fileInformation.getId(),
                address,
                fileInformation.getReferenceType(),
                fileInformation.getReferenceId()
        );
    }

    public static UploadFileResponse toUploadFileResponse(FileInformation fileInformation, String address) {
        return UploadFileResponse.builder()
                .id(fileInformation.getId())
                .address(address)
                .contentType(fileInformation.getContentType())
                .referenceType(fileInformation.getReferenceType())
                .referenceId(fileInformation.getReferenceId())
                .build();
    }
}
