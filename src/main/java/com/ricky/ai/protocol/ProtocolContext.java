package com.ricky.ai.protocol;

import java.time.Duration;
import java.util.Map;

public record ProtocolContext(
        String baseUrl,
        String apiKey,
        String modelName,
        Duration timeout,
        Map<String, Object> extraConfig
) {
}
