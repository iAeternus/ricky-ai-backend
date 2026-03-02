package com.ricky.user.infra.repo;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.user.domain.model.User;
import com.ricky.user.domain.model.UserRole;
import com.ricky.user.domain.model.UserStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class UserRepository {
    private final UserR2dbcRepository repository;

    public UserRepository(UserR2dbcRepository repository) {
        this.repository = repository;
    }

    public Mono<User> save(String email, String passwordHash, String displayName) {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPasswordHash(passwordHash);
        entity.setDisplayName(displayName);
        entity.setRole(UserRole.USER.name());
        entity.setStatus(UserStatus.ACTIVE.name());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return repository.save(entity).map(this::toDomain);
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
                    return repository.save(existing).map(this::toDomain);
                });
    }

    public Mono<User> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                UserRole.valueOf(entity.getRole()),
                UserStatus.valueOf(entity.getStatus()),
                entity.getLastLoginAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
