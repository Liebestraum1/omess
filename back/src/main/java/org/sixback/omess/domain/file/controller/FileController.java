package org.sixback.omess.domain.file.controller;

import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.file.model.dto.request.UploadFileRequest;
import org.sixback.omess.domain.file.model.dto.response.GetDownloadResponse;
import org.sixback.omess.domain.file.model.dto.response.GetFileInfoResponse;
import org.sixback.omess.domain.file.model.dto.response.UploadFileResponse;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.sixback.omess.domain.file.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@Slf4j
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
    public ResponseEntity<List<GetFileInfoResponse>> getFileInfos(
            @RequestParam(name = "id") String referenceId,
            @RequestParam(name = "type") ReferenceType referenceType
    ) {
        List<GetFileInfoResponse> fileInfos = fileService.getFileInfos(referenceType, referenceId);
        return ResponseEntity.ok().body(fileInfos);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> previewFile(
            @PathVariable(name = "fileId") Long id
    ) {
        GetObjectResponse objectResponse = fileService.preview(id);
        log.debug("objectResponse = {}", objectResponse);
        log.debug("objectResponse.headers() = {}", objectResponse.headers());
        log.debug("objectResponse.object() = {}", objectResponse.object());

        HttpHeaders httpHeaders = new HttpHeaders();
        objectResponse.headers()
                .toMultimap()
                .forEach((key, value) ->
                        value.forEach(v -> httpHeaders.add(key, v)));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new InputStreamResource(objectResponse));
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(name = "fileId") Long id) {
        GetDownloadResponse getDownloadResponse = fileService.download(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_OCTET_STREAM);
        log.info("name: {}", getDownloadResponse.originalName());
        String originalName = URLEncoder.encode(getDownloadResponse.originalName(), UTF_8);
        httpHeaders.setContentDispositionFormData("attachment", originalName);
        return new ResponseEntity<>(getDownloadResponse.data(), httpHeaders, OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(
            @RequestParam("id") List<Long> id
    ) {
        fileService.deleteFileInfos(id);
        return ResponseEntity.ok().build();
    }
}
