package com.ricky.core.user.infra;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.user.domain.User;
import com.ricky.core.user.domain.UserRole;
import com.ricky.core.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserR2dbcRepository repository;

    public Mono<User> save(String email, String passwordHash, String displayName) {
        UserEntity entity = UserEntity.from(email, passwordHash, displayName);
        return repository.save(entity).map(UserEntity::into);
    }

    public Mono<User> update(User user) {
        return repository.findById(user.getId())
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(existing -> {
                    existing.setEmail(user.getEmail());
                    existing.setPasswordHash(user.getPasswordHash());
                    existing.setDisplayName(user.getDisplayName());
                    existing.setRole(user.getRole().name());
                    existing.setStatus(user.getStatus().name());
                    existing.setLastLoginAt(user.getLastLoginAt());
                    existing.setUpdatedAt(Instant.now());
                    return repository.save(existing).map(UserEntity::into);
                });
    }

    public Mono<User> findById(Long id) {
        return repository.findById(id).map(UserEntity::into);
    }

    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserEntity::into);
    }

}
