package com.ricky.core.auth.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class RefreshToken {

    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private String token;
    @NonNull
    private Instant expiresAt;
    private boolean revoked;

    public boolean isExpired(Instant now) {
        return expiresAt.isBefore(now) || expiresAt.equals(now);
    }

    public void revoke() {
        this.revoked = true;
    }
}
