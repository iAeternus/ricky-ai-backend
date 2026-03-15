package com.ricky.apitest;

import io.restassured.response.Response;

public final class ChatApi {
    private ChatApi() {
    }

    public static Response cancelRaw(String token, String requestId) {
        return BaseApiTest.givenBearer(token)
                .when()
                .post("/chat/cancel/" + requestId);
    }

    public static Boolean cancel(String token, String requestId) {
        return cancelRaw(token, requestId)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getBoolean("data");
    }
}
