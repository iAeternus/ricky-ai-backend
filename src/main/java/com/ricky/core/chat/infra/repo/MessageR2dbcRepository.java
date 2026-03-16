package com.ricky.core.chat.infra.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageR2dbcRepository extends ReactiveCrudRepository<MessageEntity, Long> {
    Flux<MessageEntity> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    Flux<MessageEntity> findByConversationIdAndStatusOrderByCreatedAtAsc(Long conversationId, String status);
}
