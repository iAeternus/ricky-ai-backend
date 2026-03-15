package com.ricky.core.user;

import com.ricky.common.api.ApiResponse;
import com.ricky.core.user.dto.resp.UserProfileResponse;
import com.ricky.core.user.dto.req.UserUpdateRequest;
import com.ricky.core.user.service.UserAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.ricky.common.security.SecurityUtils.currentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserAppService userAppService;

    @GetMapping("/me")
    public Mono<ApiResponse<UserProfileResponse>> me() {
        return currentUserId()
                .flatMap(userAppService::getProfile)
                .map(ApiResponse::ok);
    }

    @PutMapping("/me")
    public Mono<ApiResponse<UserProfileResponse>> update(@RequestBody @Valid UserUpdateRequest request) {
        return currentUserId()
                .flatMap(userId -> userAppService.updateProfile(userId, request))
                .map(ApiResponse::ok);
    }
}
