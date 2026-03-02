package com.ricky.model.infra;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AiModelR2dbcRepository extends ReactiveCrudRepository<AiModelEntity, Long> {
}
