package com.ricky.apitest;

import java.util.UUID;

public final class RandomTestFixture {
    private RandomTestFixture() {
    }

    public static String rEmail() {
        return "user_" + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

    public static String rPassword() {
        return "P@ssw0rd_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String rDisplayName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
