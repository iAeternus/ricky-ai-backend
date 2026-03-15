package com.ricky.apitest.file;

import com.ricky.apitest.BaseApiTest;
import com.ricky.apitest.FileApi;
import com.ricky.apitest.SetupApi;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.file.dto.resp.FileUploadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileControllerTest extends BaseApiTest {

    @Autowired
    private SetupApi setupApi;

    @Test
    void uploadFile() throws Exception {
        TokenResponse token = setupApi.register();
        String accessToken = token.accessToken();

        File file = new ClassPathResource("testdata/plain-text-file.txt").getFile();
        FileUploadResponse response = FileApi.upload(accessToken, file);
        assertNotNull(response);
        assertEquals("plain-text-file.txt", response.filename());
    }
}
