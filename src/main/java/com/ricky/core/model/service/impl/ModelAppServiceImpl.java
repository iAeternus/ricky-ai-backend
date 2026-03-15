package com.ricky.core.model.service.impl;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.model.domain.AiModel;
import com.ricky.core.model.domain.ModelStatus;
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
        AiModel model = AiModel.createNew(
                request.name(),
                request.providerType(),
                request.protocolType(),
                request.baseUrl(),
                request.apiKey(),
                request.modelName(),
                request.priority(),
                request.extraConfig()
        );
        return repository.save(model).map(this::toResponse);
    }

    @Override
    public Mono<AiModelResponse> update(Long id, AiModelUpdateRequest request) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.MODEL_NOT_FOUND)))
                .map(existing -> existing.update(
                        request.name(),
                        request.providerType(),
                        request.protocolType(),
                        request.baseUrl(),
                        request.apiKey(),
                        request.modelName(),
                        request.enabled(),
                        request.priority(),
                        request.extraConfig()
                ))
                .flatMap(repository::update)
                .map(this::toResponse);
    }

    private AiModelResponse toResponse(AiModel model) {
        return new AiModelResponse(
                model.getId(),
                model.getName(),
                model.getProviderType(),
                model.getProtocolType(),
                model.getBaseUrl(),
                model.getModelName(),
                model.getStatus() == ModelStatus.ENABLED,
                model.getPriority(),
                model.getExtraConfig()
        );
    }
}
