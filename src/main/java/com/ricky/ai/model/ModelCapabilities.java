package com.ricky.ai.model;

public record ModelCapabilities(
        boolean supportsStreaming,
        boolean supportsTools,
        boolean supportsVision
) {
}
