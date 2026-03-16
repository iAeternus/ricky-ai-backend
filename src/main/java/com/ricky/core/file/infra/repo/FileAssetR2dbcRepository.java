package com.ricky.core.file.infra.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FileAssetR2dbcRepository extends ReactiveCrudRepository<FileAssetEntity, Long> {
    Flux<FileAssetEntity> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
    Flux<FileAssetEntity> findByConversationIdAndStatusOrderByCreatedAtDesc(Long conversationId, String status);
}
