package org.sixback.omess.domain.httpmethod.controller;

import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.httpmethod.model.HttpHeadersResponse;
import org.sixback.omess.domain.httpmethod.model.HttpMethodsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sixback.omess.domain.httpmethod.model.HttpHeader.getHeaderNames;
import static org.sixback.omess.domain.httpmethod.model.HttpMethod.getMethodNames;

@RestController
@RequestMapping("/api/v1/http-metadata")
@RequiredArgsConstructor
public class HttpMetadataController {
    @GetMapping("/methods")
    public ResponseEntity<HttpMethodsResponse> getHttpMethods() {
        return ResponseEntity.ok().body(new HttpMethodsResponse(getMethodNames()));
    }

    @GetMapping("/headers")
    public ResponseEntity<?> getHttpHeaders() {
        return ResponseEntity.ok().body(new HttpHeadersResponse(getHeaderNames()));
    }
}
