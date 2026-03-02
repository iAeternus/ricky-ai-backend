package com.ricky.common.security;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class SessionService {
    private final ReactiveStringRedisTemplate redisTemplate;

    public SessionService(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Void> storeSession(Long userId, String refreshToken, Duration ttl, String deviceId) {
        String device = normalizeDevice(deviceId);
        String refreshKey = refreshKey(refreshToken);
        String deviceKey = deviceKey(userId, device);
        return redisTemplate.opsForValue()
                .set(refreshKey, String.valueOf(userId), ttl)
                .then(redisTemplate.opsForValue().set(deviceKey, refreshToken, ttl))
                .then();
    }

    public Mono<Boolean> isValidRefreshToken(String refreshToken) {
        return redisTemplate.hasKey(refreshKey(refreshToken))
                .defaultIfEmpty(false);
    }

    public Mono<Void> revokeSession(Long userId, String refreshToken, String deviceId) {
        String device = normalizeDevice(deviceId);
        return redisTemplate.delete(refreshKey(refreshToken))
                .then(redisTemplate.delete(deviceKey(userId, device)))
                .then();
    }

    public Mono<String> getRefreshToken(Long userId, String deviceId) {
        String device = normalizeDevice(deviceId);
        return redisTemplate.opsForValue().get(deviceKey(userId, device));
    }

    private String refreshKey(String token) {
        return "session:refresh:" + token;
    }

    private String deviceKey(Long userId, String deviceId) {
        return "session:user:" + userId + ":" + deviceId;
    }

    private String normalizeDevice(String deviceId) {
        return deviceId == null || deviceId.isBlank() ? "default" : deviceId;
    }
}
