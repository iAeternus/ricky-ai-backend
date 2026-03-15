package com.ricky.core.ai.model;

public record ModelCapabilities(
        boolean supportsStreaming,
        boolean supportsTools,
        boolean supportsVision
) {
}
