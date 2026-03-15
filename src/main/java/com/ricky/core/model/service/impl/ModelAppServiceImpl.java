package com.ricky.core.model.service.impl;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.model.dto.req.AiModelCreateRequest;
import com.ricky.core.model.dto.resp.AiModelResponse;
import com.ricky.core.model.dto.req.AiModelUpdateRequest;
import com.ricky.core.model.infra.AiModelRepository;
import com.ricky.core.model.service.ModelAppService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ModelAppServiceImpl implements ModelAppService {
    private final AiModelRepository repository;

    public ModelAppServiceImpl(AiModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<List<AiModelResponse>> list() {
        return repository.findAll()
                .map(this::toResponse)
                .collectList();
    }

    @Override
    public Mono<AiModelResponse> get(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.MODEL_NOT_FOUND)))
                .map(this::toResponse);
    }

    @Override
    public Mono<AiModelResponse> create(AiModelCreateRequest request) {
        return repository.save(fromCreate(request)).map(this::toResponse);
    }

    @Override
    public Mono<AiModelResponse> update(Long id, AiModelUpdateRequest request) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.MODEL_NOT_FOUND)))
                .flatMap(existing -> repository.update(id, fromUpdate(request)).map(this::toResponse));
    }

    private AiModelConfig fromCreate(AiModelCreateRequest request) {
        return AiModelConfig.builder()
                .name(request.name())
                .providerType(request.providerType())
                .protocolType(request.protocolType())
                .baseUrl(request.baseUrl())
                .apiKey(request.apiKey())
                .modelName(request.modelName())
                .enabled(true)
                .priority(request.priority() == null ? 100 : request.priority())
                .extraConfig(request.extraConfig())
                .build();
    }

    private AiModelConfig fromUpdate(AiModelUpdateRequest request) {
        return AiModelConfig.builder()
                .name(request.name())
                .providerType(request.providerType())
                .protocolType(request.protocolType())
                .baseUrl(request.baseUrl())
                .apiKey(request.apiKey())
                .modelName(request.modelName())
                .enabled(request.enabled() == null || request.enabled())
                .priority(request.priority() == null ? 100 : request.priority())
                .extraConfig(request.extraConfig())
                .build();
    }

    private AiModelResponse toResponse(AiModelConfig config) {
        return new AiModelResponse(
                config.getId(),
                config.getName(),
                config.getProviderType(),
                config.getProtocolType(),
                config.getBaseUrl(),
                config.getModelName(),
                config.isEnabled(),
                config.getPriority(),
                config.getExtraConfig()
        );
    }
}
