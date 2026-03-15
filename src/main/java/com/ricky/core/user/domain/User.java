package com.ricky.core.user.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class User {
    private final Long id;
    @NotNull
    private final String email;
    @NotNull
    private final String passwordHash;
    @NotNull
    private final String displayName;
    @NotNull
    private final UserRole role;
    @NotNull
    private final UserStatus status;
    private final Instant lastLoginAt;
    @NotNull
    private final Instant createdAt;
    @NotNull
    private final Instant updatedAt;
}
