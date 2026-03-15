package com.ricky.core.user.service.impl;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.user.domain.User;
import com.ricky.core.user.dto.resp.UserProfileResponse;
import com.ricky.core.user.dto.req.UserUpdateRequest;
import com.ricky.core.user.infra.repo.UserRepository;
import com.ricky.core.user.service.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAppAppServiceImpl implements UserAppService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserProfileResponse> getProfile(Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .map(this::toProfile);
    }

    @Override
    public Mono<UserProfileResponse> updateProfile(Long userId, UserUpdateRequest request) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    user.changeDisplayName(request.displayName());
                    return userRepository.update(user);
                })
                .map(this::toProfile);
    }

    private UserProfileResponse toProfile(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}
