package com.ricky.core.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
public class AiModel {
    private final Long id;
    @NonNull
    private final String name;
    @NonNull
    private final ProviderType providerType;
    @NonNull
    private final ProtocolType protocolType;
    @NonNull
    private final String baseUrl;
    @NonNull
    private final String apiKey;
    @NonNull
    private final String modelName;
    @NonNull
    private final ModelStatus status;
    private final int priority;
    private final Map<String, Object> extraConfig;
    @NonNull
    private final Instant createdAt;
    @NonNull
    private final Instant updatedAt;

    public static AiModel createNew(
            String name,
            ProviderType providerType,
            ProtocolType protocolType,
            String baseUrl,
            String apiKey,
            String modelName,
            Integer priority,
            Map<String, Object> extraConfig
    ) {
        Instant now = Instant.now();
        return new AiModel(
                null,
                name,
                providerType,
                protocolType,
                baseUrl,
                apiKey,
                modelName,
                ModelStatus.ENABLED,
                priority == null ? 100 : priority,
                extraConfig,
                now,
                now
        );
    }

    public AiModel update(
            String name,
            ProviderType providerType,
            ProtocolType protocolType,
            String baseUrl,
            String apiKey,
            String modelName,
            Boolean enabled,
            Integer priority,
            Map<String, Object> extraConfig
    ) {
        ModelStatus nextStatus = enabled == null ? status : (enabled ? ModelStatus.ENABLED : ModelStatus.DISABLED);
        return new AiModel(
                id,
                name,
                providerType,
                protocolType,
                baseUrl,
                apiKey,
                modelName,
                nextStatus,
                priority == null ? this.priority : priority,
                extraConfig,
                createdAt,
                Instant.now()
        );
    }

    public AiModel withStatus(ModelStatus newStatus) {
        return new AiModel(
                id,
                name,
                providerType,
                protocolType,
                baseUrl,
                apiKey,
                modelName,
                newStatus,
                priority,
                extraConfig,
                createdAt,
                Instant.now()
        );
    }
}
