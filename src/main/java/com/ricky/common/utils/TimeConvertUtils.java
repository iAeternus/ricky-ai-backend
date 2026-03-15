package com.ricky.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class TimeConvertUtils {
    private TimeConvertUtils() {
    }

    public static Instant toInstant(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.toInstant(ZoneOffset.UTC);
    }

    public static LocalDateTime toLocalDateTime(Instant time) {
        if (time == null) {
            return null;
        }
        return LocalDateTime.ofInstant(time, ZoneOffset.UTC);
    }
}
