package com.ricky.apitest.user;

import com.ricky.apitest.BaseApiTest;
import com.ricky.apitest.SetupApi;
import com.ricky.apitest.UserApi;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.user.dto.req.UserUpdateRequest;
import com.ricky.core.user.dto.resp.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ricky.apitest.RandomTestFixture.rDisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest extends BaseApiTest {

    @Autowired
    private SetupApi setupApi;

    @Test
    void getAndUpdateProfile() {
        TokenResponse token = setupApi.register();
        String accessToken = token.accessToken();

        UserProfileResponse me = UserApi.me(accessToken);
        assertNotNull(me);
        assertNotNull(me.email());

        String newName = rDisplayName();
        UserProfileResponse updated = UserApi.update(accessToken, new UserUpdateRequest(newName));
        assertEquals(newName, updated.displayName());
    }
}
