package com.ricky.core.chat.dto;

import java.util.Map;

public record ChatStreamResponse(
        String type,
        String content,
        boolean finished,
        String model,
        Map<String, Object> metadata,
        String error
) {
}
