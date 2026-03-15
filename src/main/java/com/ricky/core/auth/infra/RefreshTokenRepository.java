package com.ricky.core.auth.infra;

import com.ricky.core.auth.domain.RefreshToken;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class RefreshTokenRepository {
    private final RefreshTokenR2dbcRepository repository;
    private final RefreshTokenConverter converter;

    public RefreshTokenRepository(RefreshTokenR2dbcRepository repository, RefreshTokenConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public Mono<RefreshToken> save(Long userId, String token, Instant expiresAt) {
        RefreshToken refreshToken = new RefreshToken(
                null,
                userId,
                token,
                expiresAt,
                false,
                Instant.now()
        );
        return repository.save(converter.toEntity(refreshToken))
                .map(converter::toDomain);
    }

    public Mono<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(converter::toDomain);
    }

    public Mono<Boolean> revoke(String token) {
        return repository.findByToken(token)
                .map(converter::toDomain)
                .map(RefreshToken::revoke)
                .map(converter::toEntity)
                .flatMap(entity -> repository.save(entity).thenReturn(true))
                .defaultIfEmpty(false);
    }
}
