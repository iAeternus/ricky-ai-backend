package com.ricky.apitest.auth;

import com.ricky.apitest.AuthApi;
import com.ricky.apitest.BaseApiTest;
import com.ricky.core.auth.dto.req.LoginRequest;
import com.ricky.core.auth.dto.req.RefreshRequest;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.user.dto.req.UserRegisterRequest;
import org.junit.jupiter.api.Test;

import static com.ricky.apitest.RandomTestFixture.rDisplayName;
import static com.ricky.apitest.RandomTestFixture.rEmail;
import static com.ricky.apitest.RandomTestFixture.rPassword;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest extends BaseApiTest {

    @Test
    void registerLoginRefreshLogout() {
        String email = rEmail();
        String password = rPassword();
        String displayName = rDisplayName();

        UserRegisterRequest registerRequest = new UserRegisterRequest(email, password, displayName, "test-device");
        TokenResponse registerResp = AuthApi.register(registerRequest);
        assertNotNull(registerResp.accessToken());
        assertNotNull(registerResp.refreshToken());

        TokenResponse loginResp = AuthApi.login(new LoginRequest(email, password, "test-device"));
        assertNotNull(loginResp.accessToken());
        assertNotNull(loginResp.refreshToken());

        TokenResponse refreshResp = AuthApi.refresh(new RefreshRequest(loginResp.refreshToken(), "test-device"));
        assertNotNull(refreshResp.accessToken());
        assertNotNull(refreshResp.refreshToken());

        Boolean logoutResp = AuthApi.logout(new RefreshRequest(refreshResp.refreshToken(), "test-device"));
        assertTrue(logoutResp);
    }
}
