package com.ricky.core.user.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

import static com.ricky.common.utils.ValidationUtils.requireNonNull;

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String passwordHash;
    @NotNull
    private String displayName;
    @NotNull
    private UserRole role;
    @NotNull
    private UserStatus status;
    private Instant lastLoginAt;

    public void changeDisplayName(String displayName) {
        this.displayName = requireNonNull(displayName, "displayName");
    }
}
