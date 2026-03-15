package com.ricky.core.auth.infra;

import com.ricky.core.auth.domain.RefreshToken;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RefreshTokenConverter {

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.isRevoked(),
                entity.getCreatedAt()
        );
    }

    public RefreshTokenEntity toEntity(RefreshToken token) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(token.getId());
        entity.setUserId(token.getUserId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setRevoked(token.isRevoked());
        entity.setCreatedAt(token.getCreatedAt() == null ? Instant.now() : token.getCreatedAt());
        return entity;
    }
}
