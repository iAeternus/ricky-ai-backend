package com.ricky.core.model.infra.repo;

import lombok.Data;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("ai_models")
public class AiModelEntity {

    @Id
    private Long id;
    private String name;
    @Column("provider_type")
    private String providerType;
    @Column("protocol_type")
    private String protocolType;
    @Column("base_url")
    private String baseUrl;
    @Column("api_key")
    private String apiKey;
    @Column("model_name")
    private String modelName;
    private boolean enabled;
    private int priority;
    @Column("extra_config")
    private Json extraConfig;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;

}
