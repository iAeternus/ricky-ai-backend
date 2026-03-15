package com.ricky.core.auth.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class RefreshToken {

    private final Long id;
    @NotNull
    private final Long userId;
    @NotNull
    private final String token;
    @NotNull
    private final Instant expiresAt;
    private final boolean revoked;
    @NotNull
    private final Instant createdAt;

    public boolean isExpired(Instant now) {
        return expiresAt.isBefore(now) || expiresAt.equals(now);
    }

    public RefreshToken revoke() {
        return new RefreshToken(id, userId, token, expiresAt, true, createdAt);
    }
}
