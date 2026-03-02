package com.ricky.ai.model;

import java.util.Map;

public record AiChunk(
        String content,
        boolean finished,
        String model,
        Map<String, Object> metadata
) {
}
