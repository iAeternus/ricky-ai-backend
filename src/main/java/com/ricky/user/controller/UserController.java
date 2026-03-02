package com.ricky.user.controller;

import com.ricky.common.api.ApiResponse;
import com.ricky.common.security.JwtPrincipal;
import com.ricky.user.application.UserAppService;
import com.ricky.user.dto.UserProfileResponse;
import com.ricky.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserAppService userAppService;

    public UserController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping("/me")
    public Mono<ApiResponse<UserProfileResponse>> me() {
        return currentUserId()
                .flatMap(userAppService::getProfile)
                .map(ApiResponse::ok);
    }

    @PutMapping("/me")
    public Mono<ApiResponse<UserProfileResponse>> update(@Valid @RequestBody UserUpdateRequest request) {
        return currentUserId()
                .flatMap(userId -> userAppService.updateProfile(userId, request))
                .map(ApiResponse::ok);
    }

    private Mono<Long> currentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> {
                    Object principal = auth.getPrincipal();
                    if (principal instanceof JwtPrincipal jwtPrincipal) {
                        return jwtPrincipal.userId();
                    }
                    if (principal instanceof UserDetails userDetails) {
                        return Long.parseLong(userDetails.getUsername());
                    }
                    return Long.parseLong(String.valueOf(principal));
                });
    }
}
