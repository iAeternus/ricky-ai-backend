package com.ricky.core.auth.infra.repo;

import com.ricky.core.auth.domain.RefreshToken;
import com.ricky.core.auth.domain.RefreshTokenFactory;
import com.ricky.core.auth.infra.mapper.RefreshTokenDataMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class RefreshTokenRepository {
    private final RefreshTokenR2dbcRepository repository;
    private final RefreshTokenDataMapper converter;
    private final RefreshTokenFactory factory;

    public RefreshTokenRepository(
            RefreshTokenR2dbcRepository repository,
            RefreshTokenDataMapper converter,
            RefreshTokenFactory factory
    ) {
        this.repository = repository;
        this.converter = converter;
        this.factory = factory;
    }

    public Mono<RefreshToken> save(Long userId, String token, Instant expiresAt) {
        RefreshToken refreshToken = factory.create(userId, token, expiresAt);
        return repository.save(converter.toEntity(refreshToken))
                .map(converter::toDomain);
    }

    public Mono<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(converter::toDomain);
    }

    public Mono<Boolean> revoke(String token) {
        return repository.findByToken(token)
                .flatMap(entity -> {
                    RefreshToken domain = converter.toDomain(entity);
                    domain.revoke();
                    converter.updateEntity(entity, domain);
                    return repository.save(entity).thenReturn(true);
                })
                .defaultIfEmpty(false);
    }
}
