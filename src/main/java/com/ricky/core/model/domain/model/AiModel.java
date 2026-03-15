package com.ricky.core.model.domain.model;

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
