package com.ricky.core.model.controller;

import com.ricky.common.api.ApiResponse;
import com.ricky.core.model.application.ModelAppService;
import com.ricky.core.model.dto.AiModelCreateRequest;
import com.ricky.core.model.dto.AiModelResponse;
import com.ricky.core.model.dto.AiModelUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelController {
    private final ModelAppService modelAppService;

    public ModelController(ModelAppService modelAppService) {
        this.modelAppService = modelAppService;
    }

    @GetMapping
    public Mono<ApiResponse<List<AiModelResponse>>> list() {
        return modelAppService.list().map(ApiResponse::ok);
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<AiModelResponse>> get(@PathVariable Long id) {
        return modelAppService.get(id).map(ApiResponse::ok);
    }

    @PostMapping
    public Mono<ApiResponse<AiModelResponse>> create(@Valid @RequestBody AiModelCreateRequest request) {
        return modelAppService.create(request).map(ApiResponse::ok);
    }

    @PutMapping("/{id}")
    public Mono<ApiResponse<AiModelResponse>> update(@PathVariable Long id, @Valid @RequestBody AiModelUpdateRequest request) {
        return modelAppService.update(id, request).map(ApiResponse::ok);
    }
}
