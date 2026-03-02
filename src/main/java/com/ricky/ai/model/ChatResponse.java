package com.ricky.ai.model;

import java.util.Map;

public record ChatResponse(
        String content,
        String model,
        Map<String, Object> metadata
) {
}
