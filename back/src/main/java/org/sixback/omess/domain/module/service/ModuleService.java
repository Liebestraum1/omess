package org.sixback.omess.domain.module.service;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.repository.ModuleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
}
