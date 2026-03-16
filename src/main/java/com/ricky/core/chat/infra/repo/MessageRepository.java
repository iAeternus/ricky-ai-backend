package com.ricky.core.chat.infra.repo;

import com.ricky.core.chat.domain.Message;
import com.ricky.core.chat.domain.MessageFactory;
import com.ricky.core.chat.domain.MessageRole;
import com.ricky.core.chat.domain.MessageStatus;
import com.ricky.core.chat.infra.mapper.MessageDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final MessageR2dbcRepository repository;
    private final MessageDataMapper mapper;
    private final MessageFactory factory;

    public Mono<Message> save(Long conversationId, Long userId, MessageRole role, String content) {
        Message message = factory.create(conversationId, userId, role, content);
        return repository.save(mapper.toEntity(message))
                .map(mapper::toAggregateRoot);
    }

    public Mono<Message> update(Message message) {
        return repository.findById(message.getId())
                .flatMap(existing -> {
                    mapper.update(existing, message);
                    return repository.save(existing).map(mapper::toAggregateRoot);
                });
    }

    public Mono<Message> findById(Long id) {
        return repository.findById(id).map(mapper::toAggregateRoot);
    }

    public Flux<Message> findByConversationId(Long conversationId) {
        return repository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .map(mapper::toAggregateRoot);
    }

    public Flux<Message> findActiveByConversationId(Long conversationId) {
        return repository.findByConversationIdAndStatusOrderByCreatedAtAsc(
                        conversationId, MessageStatus.ACTIVE.name())
                .map(mapper::toAggregateRoot);
    }

}
