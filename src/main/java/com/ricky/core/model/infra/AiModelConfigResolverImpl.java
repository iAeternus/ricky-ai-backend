package com.ricky.core.model.infra;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.ai.config.AiModelConfigResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AiModelConfigResolverImpl implements AiModelConfigResolver {
    private final AiModelRepository repository;

    public AiModelConfigResolverImpl(AiModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<AiModelConfig> resolveById(Long modelId) {
        return repository.findById(modelId);
    }
}
