package com.ricky.core.chat;

import com.ricky.core.chat.service.ChatAppService;
import com.ricky.core.chat.dto.req.ChatStreamRequest;
import com.ricky.core.chat.dto.resp.ChatStreamResponse;
import com.ricky.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatAppService chatAppService;

    public ChatController(ChatAppService chatAppService) {
        this.chatAppService = chatAppService;
    }

    @PostMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatStreamResponse>> stream(@Valid @RequestBody ChatStreamRequest request) {
        return chatAppService.streamChat(request);
    }

    @PostMapping("/cancel/{requestId}")
    public Mono<ApiResponse<Boolean>> cancel(@PathVariable String requestId) {
        return Mono.just(chatAppService.cancelStream(requestId))
                .map(ApiResponse::ok);
    }
}
