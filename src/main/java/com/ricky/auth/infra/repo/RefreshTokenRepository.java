package com.ricky.auth.infra.repo;

import com.ricky.auth.domain.model.RefreshToken;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class RefreshTokenRepository {
    private final RefreshTokenR2dbcRepository repository;

    public RefreshTokenRepository(RefreshTokenR2dbcRepository repository) {
        this.repository = repository;
    }

    public Mono<RefreshToken> save(Long userId, String token, Instant expiresAt) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setExpiresAt(expiresAt);
        entity.setRevoked(false);
        entity.setCreatedAt(Instant.now());
        return repository.save(entity).map(this::toDomain);
    }

    public Mono<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    public Mono<Boolean> revoke(String token) {
        return repository.findByToken(token)
                .flatMap(entity -> {
                    entity.setRevoked(true);
                    return repository.save(entity).thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.isRevoked(),
                entity.getCreatedAt()
        );
    }
}
