package com.ricky.apitest;

import com.ricky.core.auth.dto.req.LoginRequest;
import com.ricky.core.auth.dto.req.RefreshRequest;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.user.dto.req.UserRegisterRequest;
import io.restassured.response.Response;

public final class AuthApi {
    private AuthApi() {
    }

    public static Response registerRaw(UserRegisterRequest request) {
        return BaseApiTest.given()
                .body(request)
                .when()
                .post("/auth/register");
    }

    public static TokenResponse register(UserRegisterRequest request) {
        return registerRaw(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", TokenResponse.class);
    }

    public static Response loginRaw(LoginRequest request) {
        return BaseApiTest.given()
                .body(request)
                .when()
                .post("/auth/login");
    }

    public static TokenResponse login(LoginRequest request) {
        return loginRaw(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", TokenResponse.class);
    }

    public static Response refreshRaw(RefreshRequest request) {
        return BaseApiTest.given()
                .body(request)
                .when()
                .post("/auth/refresh");
    }

    public static TokenResponse refresh(RefreshRequest request) {
        return refreshRaw(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", TokenResponse.class);
    }

    public static Response logoutRaw(RefreshRequest request) {
        return BaseApiTest.given()
                .body(request)
                .when()
                .post("/auth/logout");
    }

    public static Boolean logout(RefreshRequest request) {
        return logoutRaw(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getBoolean("data");
    }
}
