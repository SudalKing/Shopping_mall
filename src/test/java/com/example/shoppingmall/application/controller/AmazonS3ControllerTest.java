package com.example.shoppingmall.application.controller;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("dev")
class AmazonS3ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("1. [S3 파일 업로드 테스트]")
    @Test
    void test_1() throws Exception {
        //Given
        String name = "files";
        String contentType = "text/plain";
        String path = "src/main/resources/static/temp";
        String fileType = "temp";
        String originalFileName01 = "temp01.txt";
        String originalFileName02 = "temp02.txt";

        MockMultipartFile multipartFile01 = new MockMultipartFile(
                name,
                originalFileName01,
                contentType,
                new FileInputStream(path + "/" + originalFileName01)
        );

        MockMultipartFile multipartFile02 = new MockMultipartFile(
                name,
                originalFileName02,
                contentType,
                new FileInputStream(path + "/" + originalFileName02)
        );

        // When
        ResultActions resultActions = mockMvc.perform(
                multipart("/uploads")
                        .file(multipartFile01)
                        .file(multipartFile02)
                        .param("fileType", fileType)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].originalFileName").value(originalFileName01))
                .andExpect(jsonPath("$[0].uploadFileName").isNotEmpty())
                .andExpect(jsonPath("$[0].uploadFilePath").isNotEmpty())
                .andExpect(jsonPath("$[0].uploadFileUrl").isNotEmpty())
                .andExpect(jsonPath("$[1].originalFileName").value(originalFileName02))
                .andExpect(jsonPath("$[1].uploadFileName").isNotEmpty())
                .andExpect(jsonPath("$[1].uploadFilePath").isNotEmpty())
                .andExpect(jsonPath("$[1].uploadFileUrl").isNotEmpty())
                .andDo(print());


    }
}