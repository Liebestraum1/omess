package org.sixback.omess.domain.file.service;

import io.minio.errors.*;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.file.model.dto.response.UploadFileResponse;
import org.sixback.omess.domain.file.model.entity.FileInformation;
import org.sixback.omess.domain.file.model.enums.ReferenceType;
import org.sixback.omess.domain.file.repository.FileInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.sixback.omess.domain.file.mapper.FileMapper.toUploadFileResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileStorageService fileStorageService;
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public List<UploadFileResponse> uploadFile(List<MultipartFile> fileInfos, ReferenceType referenceType, Long referenceId) {
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
                result.add(toUploadFileResponse(fileInformation, fileStorageService.getAddress(path)));
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
}
