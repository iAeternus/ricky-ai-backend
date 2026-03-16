package com.ricky.core.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

import static com.ricky.common.utils.ValidationUtils.requireNonNull;

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String passwordHash;
    @NonNull
    private String displayName;
    @NonNull
    private UserRole role;
    @NonNull
    private UserStatus status;
    private Instant lastLoginAt;

    public void changeDisplayName(String displayName) {
        this.displayName = requireNonNull(displayName, "displayName");
    }
}
