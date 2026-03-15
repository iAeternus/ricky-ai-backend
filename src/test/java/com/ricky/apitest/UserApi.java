package com.ricky.apitest;

import com.ricky.core.user.dto.req.UserUpdateRequest;
import com.ricky.core.user.dto.resp.UserProfileResponse;
import io.restassured.response.Response;

public final class UserApi {
    private UserApi() {
    }

    public static Response meRaw(String token) {
        return BaseApiTest.givenBearer(token)
                .when()
                .get("/users/me");
    }

    public static UserProfileResponse me(String token) {
        return meRaw(token)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", UserProfileResponse.class);
    }

    public static Response updateRaw(String token, UserUpdateRequest request) {
        return BaseApiTest.givenBearer(token)
                .body(request)
                .when()
                .put("/users/me");
    }

    public static UserProfileResponse update(String token, UserUpdateRequest request) {
        return updateRaw(token, request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", UserProfileResponse.class);
    }
}
