package com.ricky.ai.config;

import reactor.core.publisher.Mono;

public interface AiModelConfigResolver {
    Mono<AiModelConfig> resolveById(Long modelId);
}
