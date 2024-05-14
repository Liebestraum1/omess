package org.sixback.omess.domain.file.service;

import io.minio.GetObjectResponse;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.file.model.dto.response.GetDownloadResponse;
import org.sixback.omess.domain.file.model.dto.response.GetFileInfoResponse;
import org.sixback.omess.domain.file.model.dto.response.UploadFileResponse;
import org.sixback.omess.domain.file.model.entity.FileInformation;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.sixback.omess.domain.file.repository.FileInfoRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.sixback.omess.domain.file.mapper.FileMapper.toGetDownloadResponse;
import static org.sixback.omess.domain.file.mapper.FileMapper.toUploadFileResponse;
import static org.sixback.omess.domain.file.model.enums.FileErrorMessage.FILE_INFO_NOT_FOUND_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileStorageService fileStorageService;
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public List<UploadFileResponse> uploadFile(List<MultipartFile> fileInfos, ReferenceType referenceType, String referenceId) {
        List<UploadFileResponse> result = new ArrayList<>();
        fileInfos.forEach(fileInfo -> {
            try {
                String path = fileStorageService.uploadFile(fileInfo, referenceType.name());
                FileInformation fileInformation = fileInfoRepository.save(FileInformation.builder()
                        .originalName(fileInfo.getOriginalFilename())
                        .path(path)
                        .contentType(fileInfo.getContentType())
                        .referenceId(referenceId)
                        .referenceType(referenceType)
                        .build());
                result.add(toUploadFileResponse(fileInformation, fileStorageService.getAddress(fileInformation.getId())));
            } catch (ServerException | InsufficientDataException | ErrorResponseException
                     | IOException | NoSuchAlgorithmException | InvalidKeyException
                     | InvalidResponseException | XmlParserException | InternalException e) {
                log.error("exception: 발생 : {}", e.getMessage());
                log.error("FILE_NOT_SAVED!!");
            } catch (PersistenceException e) {
                log.error("exception: 발생 : {}", e.getMessage());
                log.error("FILE_INFO_ENTITY_NOT_SAVED");
            } catch (Exception e) {
                log.error("exception: 발생 : {}", e.getMessage());
            }
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<GetFileInfoResponse> getFileInfos(ReferenceType referenceType, String referenceId) {
        return fileInfoRepository.findALlByReferenceTypeAndReferenceId(referenceType, referenceId)
                .stream()
                .map(fileInfo -> new GetFileInfoResponse(
                        fileInfo.getId(),
                        fileStorageService.getAddress(fileInfo.getPath()),
                        fileInfo.getReferenceType(),
                        fileInfo.getReferenceId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public GetObjectResponse preview(Long id) {
        FileInformation file = getEntity(id);
        log.info("file: {}", file);
        try {
            return fileStorageService.downloadFile(file.getPath());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("파일을 불러올 수 없습니다. e: {}", e.getMessage());
        }
        return null;
    }

    @Transactional(readOnly = true)
    public GetDownloadResponse download(Long id) {
        FileInformation file = getEntity(id);
        log.info("file: {}", file);
        GetObjectResponse objectResponse = null;
        try {
            objectResponse = fileStorageService.downloadFile(file.getPath());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("파일을 불러올 수 없습니다. exception: {}", e.getMessage());
        }
        return toGetDownloadResponse(file, new InputStreamResource(objectResponse));
    }

    @Transactional
    public void deleteFileInfos(List<Long> fileInfoIds) {
        fileInfoIds.forEach(fileInfoId -> {
            try {
                FileInformation foundFileInfo = fileInfoRepository.findById(fileInfoId)
                        .orElseThrow(() -> new EntityNotFoundException(FILE_INFO_NOT_FOUND_ERROR.getMessage()));
                System.out.println("foundFileInfo = " + foundFileInfo);
                fileStorageService.deleteFile(foundFileInfo.getPath());
                fileInfoRepository.delete(foundFileInfo);
            } catch (ServerException | InsufficientDataException | ErrorResponseException
                     | IOException | NoSuchAlgorithmException | InvalidKeyException
                     | InvalidResponseException | XmlParserException | InternalException e) {
                log.error("exception: 발생 : {}", e.getMessage());
                log.error("FILE NOT DELETED!!");
            } catch (PersistenceException e) {
                log.error("exception: 발생 : {}", e.getMessage());
                log.error("ENTITY NOT SAVED!!");
            } catch (Exception e) {
                log.error("exception: 발생 : {}", e.getMessage());
            }
        });
    }

    @Transactional
    protected FileInformation getEntity(Long id) {
        return fileInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("파일이 존재하지 않습니다."));
    }
}
