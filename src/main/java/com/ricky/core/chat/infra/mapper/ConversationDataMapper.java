package com.ricky.core.chat.infra.mapper;

import com.ricky.core.chat.domain.Conversation;
import com.ricky.core.chat.domain.ConversationStatus;
import com.ricky.core.chat.infra.repo.ConversationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConversationDataMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ConversationEntity toEntity(Conversation conversation);

    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "updatedAt", source = "entity.updatedAt")
    Conversation toAggregateRoot(ConversationEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void update(@MappingTarget ConversationEntity entity, Conversation conversation);

    default String toStatus(ConversationStatus status) {
        return status == null ? null : status.name();
    }

    default ConversationStatus toConversationStatus(String status) {
        return status == null ? null : ConversationStatus.valueOf(status);
    }
}
