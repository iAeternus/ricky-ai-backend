package com.ricky.core.user.service;

import com.ricky.core.user.dto.resp.UserProfileResponse;
import com.ricky.core.user.dto.req.UserUpdateRequest;
import reactor.core.publisher.Mono;

public interface UserAppService {

    Mono<UserProfileResponse> getProfile(Long userId);

    Mono<UserProfileResponse> updateProfile(Long userId, UserUpdateRequest request);

}
