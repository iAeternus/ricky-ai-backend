package com.ricky.core.model.infra;

import com.ricky.core.model.domain.AiModel;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AiModelRepository {
    private final AiModelR2dbcRepository repository;
    private final AiModelConverter converter;

    public AiModelRepository(AiModelR2dbcRepository repository, AiModelConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public Flux<AiModel> findAll() {
        return repository.findAll().map(converter::toDomain);
    }

    public Mono<AiModel> findById(Long id) {
        return repository.findById(id).map(converter::toDomain);
    }

    public Mono<AiModel> save(AiModel model) {
        return repository.save(converter.toEntity(model)).map(converter::toDomain);
    }

    public Mono<AiModel> update(AiModel model) {
        return repository.save(converter.toEntity(model)).map(converter::toDomain);
    }
}
