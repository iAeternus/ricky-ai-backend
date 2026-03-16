package com.ricky.core.file.infra.repo;

import com.ricky.core.file.domain.FileAsset;
import com.ricky.core.file.domain.FileAssetFactory;
import com.ricky.core.file.domain.FileStatus;
import com.ricky.core.file.infra.mapper.FileAssetDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FileAssetRepository {

    private final FileAssetR2dbcRepository repository;
    private final FileAssetDataMapper mapper;
    private final FileAssetFactory factory;

    public Mono<FileAsset> save(Long userId, Long conversationId, String filename, String contentType, long sizeBytes, String storagePath) {
        FileAsset fileAsset = factory.create(userId, conversationId, filename, contentType, sizeBytes, storagePath);
        return repository.save(mapper.toEntity(fileAsset))
                .map(mapper::toAggregateRoot);
    }

    public Mono<FileAsset> update(FileAsset fileAsset) {
        return repository.findById(fileAsset.getId())
                .flatMap(existing -> {
                    mapper.update(existing, fileAsset);
                    return repository.save(existing).map(mapper::toAggregateRoot);
                });
    }

    public Mono<FileAsset> findById(Long id) {
        return repository.findById(id).map(mapper::toAggregateRoot);
    }

    public Flux<FileAsset> findByUserId(Long userId) {
        return repository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, FileStatus.ACTIVE.name())
                .map(mapper::toAggregateRoot);
    }

    public Flux<FileAsset> findByConversationId(Long conversationId) {
        return repository.findByConversationIdAndStatusOrderByCreatedAtDesc(conversationId, FileStatus.ACTIVE.name())
                .map(mapper::toAggregateRoot);
    }

    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

}
