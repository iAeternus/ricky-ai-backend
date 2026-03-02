package com.ricky.user.application;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.user.domain.model.User;
import com.ricky.user.dto.UserProfileResponse;
import com.ricky.user.dto.UserUpdateRequest;
import com.ricky.user.infra.repo.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserAppService {
    private final UserRepository userRepository;

    public UserAppService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserProfileResponse> getProfile(Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .map(this::toProfile);
    }

    public Mono<UserProfileResponse> updateProfile(Long userId, UserUpdateRequest request) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    User updated = new User(
                            user.getId(),
                            user.getEmail(),
                            user.getPasswordHash(),
                            request.displayName(),
                            user.getRole(),
                            user.getStatus(),
                            user.getLastLoginAt(),
                            user.getCreatedAt(),
                            java.time.Instant.now()
                    );
                    return userRepository.update(updated);
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
