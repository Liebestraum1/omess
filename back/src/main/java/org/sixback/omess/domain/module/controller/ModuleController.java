package org.sixback.omess.domain.module.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.module.service.ModuleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;
}
