package com.ricky.apitest;

import com.ricky.core.model.dto.req.AiModelCreateRequest;
import com.ricky.core.model.dto.resp.AiModelResponse;
import com.ricky.core.model.dto.req.AiModelUpdateRequest;
import io.restassured.response.Response;

import java.util.List;

public final class ModelApi {
    private ModelApi() {
    }

    public static Response listRaw(String token) {
        return BaseApiTest.givenBearer(token)
                .when()
                .get("/models");
    }

    public static List<AiModelResponse> list(String token) {
        return listRaw(token)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data", AiModelResponse.class);
    }

    public static Response getRaw(String token, Long id) {
        return BaseApiTest.givenBearer(token)
                .when()
                .get("/models/" + id);
    }

    public static AiModelResponse get(String token, Long id) {
        return getRaw(token, id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", AiModelResponse.class);
    }

    public static Response createRaw(String token, AiModelCreateRequest request) {
        return BaseApiTest.givenBearer(token)
                .body(request)
                .when()
                .post("/models");
    }

    public static AiModelResponse create(String token, AiModelCreateRequest request) {
        return createRaw(token, request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", AiModelResponse.class);
    }

    public static Response updateRaw(String token, Long id, AiModelUpdateRequest request) {
        return BaseApiTest.givenBearer(token)
                .body(request)
                .when()
                .put("/models/" + id);
    }

    public static AiModelResponse update(String token, Long id, AiModelUpdateRequest request) {
        return updateRaw(token, id, request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", AiModelResponse.class);
    }
}
