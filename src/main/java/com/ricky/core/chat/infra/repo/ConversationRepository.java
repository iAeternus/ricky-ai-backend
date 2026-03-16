package com.ricky.core.chat.infra.repo;

import com.ricky.core.chat.domain.Conversation;
import com.ricky.core.chat.domain.ConversationFactory;
import com.ricky.core.chat.domain.ConversationStatus;
import com.ricky.core.chat.infra.mapper.ConversationDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ConversationRepository {

    private final ConversationR2dbcRepository repository;
    private final ConversationDataMapper mapper;
    private final ConversationFactory factory;

    public Mono<Conversation> save(Long userId, Long modelId, String title) {
        Conversation conversation = factory.create(userId, modelId, title);
        return repository.save(mapper.toEntity(conversation))
                .map(mapper::toAggregateRoot);
    }

    public Mono<Conversation> update(Conversation conversation) {
        return repository.findById(conversation.getId())
                .flatMap(existing -> {
                    mapper.update(existing, conversation);
                    return repository.save(existing).map(mapper::toAggregateRoot);
                });
    }

    public Mono<Conversation> findById(Long id) {
        return repository.findById(id).map(mapper::toAggregateRoot);
    }

    public Flux<Conversation> findByUserId(Long userId) {
        return repository.findByUserIdAndStatusOrderByUpdatedAtDesc(userId, ConversationStatus.ACTIVE.name())
                .map(mapper::toAggregateRoot);
    }

    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

}
