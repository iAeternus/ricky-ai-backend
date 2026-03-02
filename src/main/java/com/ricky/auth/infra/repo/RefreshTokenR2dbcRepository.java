package com.ricky.auth.infra.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RefreshTokenR2dbcRepository extends ReactiveCrudRepository<RefreshTokenEntity, Long> {
    Mono<RefreshTokenEntity> findByToken(String token);
}
