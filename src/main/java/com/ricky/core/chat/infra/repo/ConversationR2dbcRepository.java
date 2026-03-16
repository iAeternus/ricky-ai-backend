package com.ricky.core.chat.infra.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ConversationR2dbcRepository extends ReactiveCrudRepository<ConversationEntity, Long> {
    Flux<ConversationEntity> findByUserIdAndStatusOrderByUpdatedAtDesc(Long userId, String status);
}
