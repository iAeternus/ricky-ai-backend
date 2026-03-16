package com.ricky.core.user.infra.mapper;

import com.ricky.core.user.domain.User;
import com.ricky.core.user.infra.repo.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    UserEntity toEntity(User user);

    @Mapping(target = "role", expression = "java(UserRole.valueOf(entity.getRole()))")
    @Mapping(target = "status", expression = "java(UserStatus.valueOf(entity.getStatus()))")
    User toAggregateRoot(UserEntity entity);

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget UserEntity entity, User user);

}
