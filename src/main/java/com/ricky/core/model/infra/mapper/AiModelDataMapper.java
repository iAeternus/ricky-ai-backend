package com.ricky.core.model.infra.mapper;

import com.ricky.core.common.mapper.JsonMapMapper;
import com.ricky.core.model.domain.AiModel;
import com.ricky.core.model.domain.ModelStatus;
import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;
import com.ricky.core.model.infra.repo.AiModelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        imports = {ModelStatus.class, ProviderType.class, ProtocolType.class},
        uses = JsonMapMapper.class
)
public interface AiModelDataMapper {

    @Mapping(target = "providerType", expression = "java(ProviderType.valueOf(entity.getProviderType()))")
    @Mapping(target = "protocolType", expression = "java(ProtocolType.valueOf(entity.getProtocolType()))")
    @Mapping(target = "status", expression = "java(entity.isEnabled() ? ModelStatus.ENABLED : ModelStatus.DISABLED)")
    @Mapping(target = "extraConfig", source = "extraConfig", qualifiedByName = "jsonToMap")
    AiModel toDomain(AiModelEntity entity);

    @Mapping(target = "providerType", expression = "java(model.getProviderType().name())")
    @Mapping(target = "protocolType", expression = "java(model.getProtocolType().name())")
    @Mapping(target = "enabled", expression = "java(model.getStatus() == ModelStatus.ENABLED)")
    @Mapping(target = "extraConfig", source = "extraConfig", qualifiedByName = "mapToJson")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AiModelEntity toEntity(AiModel model);

    @Mapping(target = "providerType", expression = "java(model.getProviderType().name())")
    @Mapping(target = "protocolType", expression = "java(model.getProtocolType().name())")
    @Mapping(target = "enabled", expression = "java(model.getStatus() == ModelStatus.ENABLED)")
    @Mapping(target = "extraConfig", source = "extraConfig", qualifiedByName = "mapToJson")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget AiModelEntity entity, AiModel model);

}
