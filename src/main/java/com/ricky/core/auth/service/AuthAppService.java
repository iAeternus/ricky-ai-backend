package com.ricky.core.auth.service;

import com.ricky.core.auth.dto.req.LoginRequest;
import com.ricky.core.auth.dto.req.RefreshRequest;
import com.ricky.core.auth.dto.resp.TokenResponse;
import reactor.core.publisher.Mono;

public interface AuthAppService {

    Mono<TokenResponse> register(String email, String password, String displayName, String deviceId);

    Mono<TokenResponse> login(LoginRequest request);

    Mono<TokenResponse> refresh(RefreshRequest request);

    Mono<Boolean> logout(RefreshRequest request);
}
