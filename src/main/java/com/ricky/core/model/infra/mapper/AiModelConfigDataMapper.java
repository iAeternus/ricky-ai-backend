package com.ricky.core.model.infra.mapper;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.model.domain.AiModel;
import com.ricky.core.model.domain.ModelStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = ModelStatus.class)
public interface AiModelConfigDataMapper {

    @Mapping(target = "enabled", expression = "java(model.getStatus() == ModelStatus.ENABLED)")
    AiModelConfig toConfig(AiModel model);
}
