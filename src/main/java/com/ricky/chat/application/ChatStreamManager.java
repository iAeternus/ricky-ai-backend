package com.ricky.chat.application;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatStreamManager {
    private final Map<String, Sinks.One<Void>> cancelSignals = new ConcurrentHashMap<>();

    public Mono<Void> register(String requestId) {
        Sinks.One<Void> sink = Sinks.one();
        cancelSignals.put(requestId, sink);
        return sink.asMono();
    }

    public boolean cancel(String requestId) {
        Sinks.One<Void> sink = cancelSignals.remove(requestId);
        if (sink == null) {
            return false;
        }
        sink.tryEmitEmpty();
        return true;
    }

    public void unregister(String requestId) {
        cancelSignals.remove(requestId);
    }
}
