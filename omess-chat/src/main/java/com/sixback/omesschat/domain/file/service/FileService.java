package com.sixback.omesschat.domain.file.service;

import com.sixback.omesschat.domain.file.mapper.FileMapper;
import com.sixback.omesschat.domain.file.model.dto.response.FileDto;
import com.sixback.omesschat.domain.file.repository.FileInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.sixback.omesschat.domain.file.model.entity.ReferenceType.PROFILE_IMAGE;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileInformationRepository fileInformationRepository;

    public Mono<FileDto> findByProfile(String id) {
        return fileInformationRepository
                .findByReferenceIdAndType(id, PROFILE_IMAGE)
                .map(FileMapper::toFileDto).singleOrEmpty();
    }
}
