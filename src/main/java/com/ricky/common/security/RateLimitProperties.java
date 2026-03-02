package com.ricky.common.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "security.rate-limit")
public class RateLimitProperties {
    private boolean enabled = true;
    private int limit = 60;
    private Duration window = Duration.ofMinutes(1);
}
