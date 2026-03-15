package com.ricky.apitest;

import com.ricky.core.file.dto.resp.FileUploadResponse;
import io.restassured.response.Response;

import java.io.File;

public final class FileApi {
    private FileApi() {
    }

    public static Response uploadRaw(String token, File file) {
        return BaseApiTest.givenBearer(token)
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .when()
                .post("/files/upload");
    }

    public static FileUploadResponse upload(String token, File file) {
        return uploadRaw(token, file)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", FileUploadResponse.class);
    }
}
