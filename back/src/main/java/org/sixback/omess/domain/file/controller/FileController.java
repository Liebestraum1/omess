package org.sixback.omess.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.file.model.dto.request.UploadFileRequest;
import org.sixback.omess.domain.file.model.dto.response.GetFileInfoResponse;
import org.sixback.omess.domain.file.model.dto.response.UploadFileResponse;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.sixback.omess.domain.file.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<List<UploadFileResponse>> uploadFile(
            @Validated
            UploadFileRequest uploadFileRequest
    ) {
        List<UploadFileResponse> uploadFileResponses = fileService.uploadFile(uploadFileRequest.getFiles(), uploadFileRequest.getReferenceType(), uploadFileRequest.getReferenceId());
        return new ResponseEntity<>(uploadFileResponses, CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GetFileInfoResponse>> getFiles(
            @RequestParam(name = "id") String referenceId,
            @RequestParam(name = "type") ReferenceType referenceType
    ) {
        List<GetFileInfoResponse> fileInfos = fileService.getFileInfos(referenceType, referenceId);
        return ResponseEntity.ok().body(fileInfos);
    }
}
