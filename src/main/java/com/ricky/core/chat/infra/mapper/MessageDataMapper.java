package com.ricky.core.chat.infra.mapper;

import com.ricky.core.chat.domain.Message;
import com.ricky.core.chat.domain.MessageRole;
import com.ricky.core.chat.domain.MessageStatus;
import com.ricky.core.chat.infra.repo.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageDataMapper {

    MessageEntity toEntity(Message message);

    Message toAggregateRoot(MessageEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void update(@MappingTarget MessageEntity entity, Message message);

    default String toRole(MessageRole role) {
        return role == null ? null : role.name();
    }

    default MessageRole toMessageRole(String role) {
        return role == null ? null : MessageRole.valueOf(role);
    }

    default String toStatus(MessageStatus status) {
        return status == null ? null : status.name();
    }

    default MessageStatus toMessageStatus(String status) {
        return status == null ? null : MessageStatus.valueOf(status);
    }
}
