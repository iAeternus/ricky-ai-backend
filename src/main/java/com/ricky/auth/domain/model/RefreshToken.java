package com.ricky.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class RefreshToken {
    private final Long id;
    @NonNull
    private final Long userId;
    @NonNull
    private final String token;
    @NonNull
    private final Instant expiresAt;
    private final boolean revoked;
    @NonNull
    private final Instant createdAt;

    public boolean isExpired(Instant now) {
        return expiresAt.isBefore(now) || expiresAt.equals(now);
    }

    public RefreshToken revoke() {
        return new RefreshToken(id, userId, token, expiresAt, true, createdAt);
    }
}
