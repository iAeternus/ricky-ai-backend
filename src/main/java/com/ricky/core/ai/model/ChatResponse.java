package com.ricky.core.ai.model;

import java.util.Map;

public record ChatResponse(
        String content,
        String model,
        Map<String, Object> metadata
) {
}
