package com.ricky.core.model.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.core.model.domain.AiModel;
import com.ricky.core.model.domain.ModelStatus;
import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Component
public class AiModelConverter {
    private final ObjectMapper objectMapper;

    public AiModelConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AiModel toDomain(AiModelEntity entity) {
        return new AiModel(
                entity.getId(),
                entity.getName(),
                ProviderType.valueOf(entity.getProviderType()),
                ProtocolType.valueOf(entity.getProtocolType()),
                entity.getBaseUrl(),
                entity.getApiKey(),
                entity.getModelName(),
                entity.isEnabled() ? ModelStatus.ENABLED : ModelStatus.DISABLED,
                entity.getPriority(),
                readConfig(entity.getExtraConfig()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public AiModelEntity toEntity(AiModel model) {
        AiModelEntity entity = new AiModelEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setProviderType(model.getProviderType().name());
        entity.setProtocolType(model.getProtocolType().name());
        entity.setBaseUrl(model.getBaseUrl());
        entity.setApiKey(model.getApiKey());
        entity.setModelName(model.getModelName());
        entity.setEnabled(model.getStatus() == ModelStatus.ENABLED);
        entity.setPriority(model.getPriority());
        entity.setExtraConfig(writeConfig(model.getExtraConfig()));
        entity.setCreatedAt(model.getCreatedAt() == null ? Instant.now() : model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt() == null ? Instant.now() : model.getUpdatedAt());
        return entity;
    }

    private Map<String, Object> readConfig(Json json) {
        if (json == null || json.asString() == null || json.asString().isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json.asString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    private Json writeConfig(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return Json.of(objectMapper.writeValueAsString(map));
        } catch (Exception ex) {
            return null;
        }
    }
}
