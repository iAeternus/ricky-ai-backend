package com.ricky.core.chat.service;

import com.ricky.core.chat.dto.req.ChatStreamRequest;
import com.ricky.core.chat.dto.resp.ChatStreamResponse;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ChatAppService {

    Flux<ServerSentEvent<ChatStreamResponse>> streamChat(ChatStreamRequest request);

    boolean cancelStream(String requestId);
}
