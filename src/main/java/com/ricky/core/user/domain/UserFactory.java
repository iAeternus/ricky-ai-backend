package com.ricky.core.user.domain;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public User create(String email, String passwordHash, String displayName) {
        return new User(null, email, passwordHash, displayName, UserRole.USER, UserStatus.ACTIVE, null);
    }

}
