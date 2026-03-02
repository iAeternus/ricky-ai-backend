package com.ricky.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.common.api.ApiResponse;
import com.ricky.common.exception.ErrorCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class RateLimitWebFilter implements WebFilter {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final RateLimitProperties properties;
    private final ObjectMapper objectMapper;

    public RateLimitWebFilter(ReactiveStringRedisTemplate redisTemplate, RateLimitProperties properties, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }
        String key = buildKey(exchange.getRequest());
        Duration window = properties.getWindow();
        int limit = properties.getLimit();

        return redisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> redisTemplate.expire(key, window)
                        .thenReturn(count))
                .flatMap(count -> {
                    if (count > limit) {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return writeError(exchange, ErrorCode.RATE_LIMITED);
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(ex -> chain.filter(exchange));
    }

    private String buildKey(ServerHttpRequest request) {
        String ip = request.getRemoteAddress() == null ? "unknown" : request.getRemoteAddress().getAddress().getHostAddress();
        String path = request.getPath().value();
        return "ratelimit:" + ip + ":" + path;
    }

    private Mono<Void> writeError(ServerWebExchange exchange, ErrorCode errorCode) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(ApiResponse.error(errorCode));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }
}
