package com.ricky.core.ai.model;

import java.util.Map;

public record AiChunk(
        String content,
        boolean finished,
        String model,
        Map<String, Object> metadata
) {

    public Map<String, Object> getMetadata() {
        return metadata == null ? Map.of() : metadata;
    }

}
