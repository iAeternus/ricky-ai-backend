package com.ricky.core.model.infra.repo;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.ai.config.AiModelConfigResolver;
import com.ricky.core.model.infra.mapper.AiModelConfigDataMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AiModelConfigResolverImpl implements AiModelConfigResolver {
    private final AiModelRepository repository;
    private final AiModelConfigDataMapper configConverter;

    public AiModelConfigResolverImpl(AiModelRepository repository, AiModelConfigDataMapper configConverter) {
        this.repository = repository;
        this.configConverter = configConverter;
    }

    @Override
    public Mono<AiModelConfig> resolveById(Long modelId) {
        return repository.findById(modelId).map(configConverter::toConfig);
    }
}
