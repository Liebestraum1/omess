package org.sixback.omess.domain.file.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET_NAME;

    @Value("${minio.url}")
    private String URL;

    @Value("${domain.name}")
    private String DOMAIN;

    public String uploadFile(MultipartFile multipartFile, String directoryName)
            throws IOException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException {
        bucketExistenceCheck();
        // upload
        String fileName = directoryName + "/" + UUID.randomUUID() + parseFileExtension(multipartFile.getOriginalFilename());
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(fileName)
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .contentType(multipartFile.getContentType())
                .build()
        );
        return fileName;
    }

    public void deleteFile(String fileName) throws InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException, ServerException {
        bucketExistenceCheck();
        // delete
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(fileName)
                .build());
    }

    public GetObjectResponse downloadFile(String path) throws ErrorResponseException, InsufficientDataException,
            InternalException, InvalidKeyException, InvalidResponseException,
            IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        GetObjectResponse object = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(path)
                        .build());
        log.debug("object: {}", object);
        return object;
    }

    public String getAddress(Long id) {
        return DOMAIN + "/api/v1/files/" + id;
    }

    public String getAddress(String path) {
        return URL + "/" + this.BUCKET_NAME + "/" + path;
    }

    private void bucketExistenceCheck() throws ErrorResponseException, InsufficientDataException,
            InternalException, InvalidKeyException, InvalidResponseException,
            IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        // bucket existence check
        boolean isExistBucket = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build());
        if (!isExistBucket) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());
        }
    }

    private String parseFileExtension(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }

        int idx = originalFilename.lastIndexOf('.');
        if (idx == -1) {
            return "";
        }
        return originalFilename.substring(idx);
    }
}