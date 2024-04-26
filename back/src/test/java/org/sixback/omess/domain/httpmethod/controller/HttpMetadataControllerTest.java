package org.sixback.omess.domain.httpmethod.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class HttpMetadataControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    HttpMetadataController httpMetadataController;

    @Test
    @DisplayName("HTTP Method 목록 조회 테스트")
    void getHttpMethods() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(get("/api/v1/http-metadata/methods"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("HTTP 표준 Header 목록 조회 테스트")
    void getHttpHeaders() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(get("/api/v1/http-metadata/headers"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}