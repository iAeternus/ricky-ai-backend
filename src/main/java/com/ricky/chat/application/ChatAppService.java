package com.ricky.chat.application;

import com.ricky.ai.gateway.AiGateway;
import com.ricky.ai.model.AiChunk;
import com.ricky.ai.model.ChatMessage;
import com.ricky.ai.model.ChatRequest;
import com.ricky.chat.dto.ChatMessageDto;
import com.ricky.chat.dto.ChatStreamRequest;
import com.ricky.chat.dto.ChatStreamResponse;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ChatAppService {
    private static final Duration HEARTBEAT_INTERVAL = Duration.ofSeconds(10);

    private final AiGateway aiGateway;
    private final ChatStreamManager streamManager;

    public ChatAppService(AiGateway aiGateway, ChatStreamManager streamManager) {
        this.aiGateway = aiGateway;
        this.streamManager = streamManager;
    }

    public Flux<ServerSentEvent<ChatStreamResponse>> streamChat(ChatStreamRequest request) {
        String requestId = request.requestId() == null || request.requestId().isBlank()
                ? UUID.randomUUID().toString()
                : request.requestId();

        Mono<Void> cancelSignal = streamManager.register(requestId);

        ChatRequest aiRequest = new ChatRequest(
                null,
                request.conversationId(),
                request.modelId(),
                toChatMessages(request.messages()),
                request.temperature(),
                request.maxTokens(),
                request.extraParams(),
                null
        );

        Flux<ServerSentEvent<ChatStreamResponse>> dataStream = aiGateway.streamChat(aiRequest)
                .takeUntilOther(cancelSignal)
                .map(chunk -> ServerSentEvent.builder(toResponse(chunk, "delta", null, requestId))
                        .event("delta")
                        .build())
                .concatWith(Flux.just(ServerSentEvent.builder(
                                new ChatStreamResponse("done", "", true, null, Map.of("requestId", requestId), null))
                        .event("done")
                        .build()))
                .onErrorResume(ex -> Flux.just(ServerSentEvent.builder(
                                new ChatStreamResponse("error", "", true, null, Map.of("requestId", requestId), resolveErrorMessage(ex)))
                        .event("error")
                        .build()))
                .doFinally(signal -> streamManager.unregister(requestId));

        Flux<ServerSentEvent<ChatStreamResponse>> heartbeatStream = Flux.interval(HEARTBEAT_INTERVAL)
                .map(tick -> ServerSentEvent.builder(
                                new ChatStreamResponse("heartbeat", "", false, null, Map.of("ts", Instant.now().toString(), "requestId", requestId), null))
                        .event("heartbeat")
                        .build())
                .takeUntilOther(dataStream.ignoreElements());

        return Flux.merge(dataStream, heartbeatStream);
    }

    public boolean cancelStream(String requestId) {
        return streamManager.cancel(requestId);
    }

    private List<ChatMessage> toChatMessages(List<ChatMessageDto> messages) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .map(dto -> new ChatMessage(dto.role(), dto.content()))
                .toList();
    }

    private ChatStreamResponse toResponse(AiChunk chunk, String type, String error, String requestId) {
        Map<String, Object> metadata = chunk.metadata() == null ? Map.of() : chunk.metadata();
        Map<String, Object> merged = metadata.isEmpty()
                ? Map.of("requestId", requestId)
                : new java.util.HashMap<>(metadata);
        if (!merged.containsKey("requestId")) {
            merged.put("requestId", requestId);
        }
        return new ChatStreamResponse(type, chunk.content(), chunk.finished(), chunk.model(), Map.copyOf(merged), error);
    }

    private String resolveErrorMessage(Throwable ex) {
        if (ex == null) {
            return "unknown_error";
        }
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            return ex.getMessage();
        }
        Throwable cause = ex.getCause();
        if (cause != null && cause.getMessage() != null && !cause.getMessage().isBlank()) {
            return cause.getMessage();
        }
        return ex.getClass().getSimpleName();
    }
}
