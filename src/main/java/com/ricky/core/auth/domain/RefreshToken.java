package com.ricky.core.auth.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class RefreshToken {

    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private String token;
    @NotNull
    private Instant expiresAt;
    private boolean revoked;

    public boolean isExpired(Instant now) {
        return expiresAt.isBefore(now) || expiresAt.equals(now);
    }

    public void revoke() {
        this.revoked = true;
    }
}
