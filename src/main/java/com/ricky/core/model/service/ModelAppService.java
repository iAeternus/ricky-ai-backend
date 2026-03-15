package com.ricky.core.model.service;

import com.ricky.core.model.dto.req.AiModelCreateRequest;
import com.ricky.core.model.dto.resp.AiModelResponse;
import com.ricky.core.model.dto.req.AiModelUpdateRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ModelAppService {

    Mono<List<AiModelResponse>> list();

    Mono<AiModelResponse> get(Long id);

    Mono<AiModelResponse> create(AiModelCreateRequest request);

    Mono<AiModelResponse> update(Long id, AiModelUpdateRequest request);
}
