package com.ricky.core.auth.infra.mapper;

import com.ricky.core.auth.domain.RefreshToken;
import com.ricky.core.auth.infra.repo.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RefreshTokenDataMapper {

    RefreshToken toDomain(RefreshTokenEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    RefreshTokenEntity toEntity(RefreshToken token);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget RefreshTokenEntity entity, RefreshToken token);
}
