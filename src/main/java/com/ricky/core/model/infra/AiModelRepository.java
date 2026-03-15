package com.ricky.core.model.infra;

import com.ricky.core.ai.config.AiModelConfig;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AiModelRepository {
    private final AiModelR2dbcRepository repository;
    private final AiModelMapper mapper;

    public AiModelRepository(AiModelR2dbcRepository repository, AiModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<AiModelConfig> findAll() {
        return repository.findAll().map(mapper::toConfig);
    }

    public Mono<AiModelConfig> findById(Long id) {
        return repository.findById(id).map(mapper::toConfig);
    }

    public Mono<AiModelConfig> save(AiModelConfig config) {
        return repository.save(mapper.toEntity(config)).map(mapper::toConfig);
    }

    public Mono<AiModelConfig> update(Long id, AiModelConfig config) {
        return repository.findById(id)
                .flatMap(existing -> {
                    AiModelEntity entity = mapper.toEntity(config);
                    entity.setId(id);
                    entity.setCreatedAt(existing.getCreatedAt());
                    entity.setUpdatedAt(java.time.Instant.now());
                    return repository.save(entity).map(mapper::toConfig);
                });
    }
}
