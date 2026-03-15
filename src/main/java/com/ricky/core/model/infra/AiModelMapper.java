package com.ricky.core.model.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.model.domain.model.ProtocolType;
import com.ricky.core.model.domain.model.ProviderType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Component
public class AiModelMapper {
    private final ObjectMapper objectMapper;

    public AiModelMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AiModelConfig toConfig(AiModelEntity entity) {
        return AiModelConfig.builder()
                .id(entity.getId())
                .name(entity.getName())
                .providerType(ProviderType.valueOf(entity.getProviderType()))
                .protocolType(ProtocolType.valueOf(entity.getProtocolType()))
                .baseUrl(entity.getBaseUrl())
                .apiKey(entity.getApiKey())
                .modelName(entity.getModelName())
                .enabled(entity.isEnabled())
                .priority(entity.getPriority())
                .extraConfig(readConfig(entity.getExtraConfig()))
                .build();
    }

    public AiModelEntity toEntity(AiModelConfig config) {
        AiModelEntity entity = new AiModelEntity();
        entity.setId(config.getId());
        entity.setName(config.getName());
        entity.setProviderType(config.getProviderType().name());
        entity.setProtocolType(config.getProtocolType().name());
        entity.setBaseUrl(config.getBaseUrl());
        entity.setApiKey(config.getApiKey());
        entity.setModelName(config.getModelName());
        entity.setEnabled(config.isEnabled());
        entity.setPriority(config.getPriority());
        entity.setExtraConfig(writeConfig(config.getExtraConfig()));
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
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
