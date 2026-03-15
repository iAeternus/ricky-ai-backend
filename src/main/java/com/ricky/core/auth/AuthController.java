package com.ricky.core.auth;

import com.ricky.core.auth.service.AuthAppService;
import com.ricky.core.auth.dto.req.LoginRequest;
import com.ricky.core.auth.dto.req.RefreshRequest;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.common.api.ApiResponse;
import com.ricky.core.user.dto.req.UserRegisterRequest;
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
