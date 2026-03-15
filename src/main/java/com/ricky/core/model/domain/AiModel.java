package com.ricky.core.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
@AllArgsConstructor
public class AiModel {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private ProviderType providerType;
    @NonNull
    private ProtocolType protocolType;
    @NonNull
    private String baseUrl;
    @NonNull
    private String apiKey;
    @NonNull
    private String modelName;
    @NonNull
    private ModelStatus status;
    private int priority;
    private Map<String, Object> extraConfig;

    public void applyUpdate(
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
        this.name = name;
        this.providerType = providerType;
        this.protocolType = protocolType;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.modelName = modelName;
        if (enabled != null) {
            this.status = enabled ? ModelStatus.ENABLED : ModelStatus.DISABLED;
        }
        if (priority != null) {
            this.priority = priority;
        }
        this.extraConfig = extraConfig;
    }
}
