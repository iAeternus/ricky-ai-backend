package com.ricky.auth.controller;

import com.ricky.auth.application.AuthAppService;
import com.ricky.auth.dto.LoginRequest;
import com.ricky.auth.dto.RefreshRequest;
import com.ricky.auth.dto.TokenResponse;
import com.ricky.common.api.ApiResponse;
import com.ricky.user.dto.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthAppService authAppService;

    public AuthController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    @PostMapping("/register")
    public Mono<ApiResponse<TokenResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
        return authAppService.register(request.email(), request.password(), request.displayName(), request.deviceId())
                .map(ApiResponse::ok);
    }

    @PostMapping("/login")
    public Mono<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return authAppService.login(request)
                .map(ApiResponse::ok);
    }

    @PostMapping("/refresh")
    public Mono<ApiResponse<TokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        return authAppService.refresh(request)
                .map(ApiResponse::ok);
    }

    @PostMapping("/logout")
    public Mono<ApiResponse<Boolean>> logout(@Valid @RequestBody RefreshRequest request) {
        return authAppService.logout(request)
                .map(ApiResponse::ok);
    }
}
