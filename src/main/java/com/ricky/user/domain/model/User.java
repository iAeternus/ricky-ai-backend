package com.ricky.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class User {
    private final Long id;
    @NonNull
    private final String email;
    @NonNull
    private final String passwordHash;
    @NonNull
    private final String displayName;
    @NonNull
    private final UserRole role;
    @NonNull
    private final UserStatus status;
    private final Instant lastLoginAt;
    @NonNull
    private final Instant createdAt;
    @NonNull
    private final Instant updatedAt;

    public User withLastLoginAt(Instant when) {
        return new User(id, email, passwordHash, displayName, role, status, when, createdAt, Instant.now());
    }

    public User withStatus(UserStatus newStatus) {
        return new User(id, email, passwordHash, displayName, role, newStatus, lastLoginAt, createdAt, Instant.now());
    }
}
