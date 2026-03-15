package com.ricky.apitest;

import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.user.dto.req.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ricky.apitest.RandomTestFixture.rDisplayName;
import static com.ricky.apitest.RandomTestFixture.rEmail;
import static com.ricky.apitest.RandomTestFixture.rPassword;

@Component
@RequiredArgsConstructor
public class SetupApi {

    public TokenResponse register() {
        return register(rEmail(), rPassword(), rDisplayName());
    }

    public TokenResponse register(String email, String password, String displayName) {
        UserRegisterRequest request = new UserRegisterRequest(email, password, displayName, "test-device");
        return AuthApi.register(request);
    }
}
