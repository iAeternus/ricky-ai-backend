package com.ricky.core.auth.service.impl;

import com.ricky.core.auth.dto.req.LoginRequest;
import com.ricky.core.auth.dto.req.RefreshRequest;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.auth.infra.RefreshTokenRepository;
import com.ricky.core.auth.service.AuthAppService;
import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.common.security.JwtProperties;
import com.ricky.common.security.JwtService;
import com.ricky.common.security.PasswordHasher;
import com.ricky.common.security.SessionService;
import com.ricky.core.user.domain.User;
import com.ricky.core.user.infra.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class AuthAppServiceImpl implements AuthAppService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final SessionService sessionService;

    public AuthAppServiceImpl(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordHasher passwordHasher,
            JwtService jwtService,
            JwtProperties jwtProperties,
            SessionService sessionService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.sessionService = sessionService;
    }

    @Override
    public Mono<TokenResponse> register(String email, String password, String displayName, String deviceId) {
        return userRepository.findByEmail(email)
                .flatMap(existing -> Mono.<TokenResponse>error(new BizException(ErrorCode.EMAIL_EXISTS)))
                .switchIfEmpty(Mono.defer(() -> {
                    String hash = passwordHasher.hash(password);
                    return userRepository.save(email, hash, displayName)
                            .flatMap(user -> issueTokens(user, deviceId));
                }));
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.INVALID_CREDENTIALS)))
                .flatMap(user -> {
                    if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
                        return Mono.error(new BizException(ErrorCode.INVALID_CREDENTIALS));
                    }
                    return issueTokens(user, request.deviceId());
                });
    }

    @Override
    public Mono<TokenResponse> refresh(RefreshRequest request) {
        return sessionService.isValidRefreshToken(request.refreshToken())
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.INVALID_REFRESH_TOKEN)))
                .then(refreshTokenRepository.findByToken(request.refreshToken()))
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.INVALID_REFRESH_TOKEN)))
                .flatMap(token -> {
                    if (token.isRevoked() || token.isExpired(Instant.now())) {
                        return Mono.error(new BizException(ErrorCode.INVALID_REFRESH_TOKEN));
                    }
                    return userRepository.findById(token.getUserId())
                            .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                            .flatMap(user -> issueTokens(user, request.deviceId()));
                });
    }

    @Override
    public Mono<Boolean> logout(RefreshRequest request) {
        return refreshTokenRepository.findByToken(request.refreshToken())
                .flatMap(token -> sessionService.revokeSession(token.getUserId(), request.refreshToken(), request.deviceId())
                        .then(refreshTokenRepository.revoke(request.refreshToken())))
                .defaultIfEmpty(false);
    }

    private Mono<TokenResponse> issueTokens(User user, String deviceId) {
        String access = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refresh = jwtService.generateRefreshToken(user.getId());
        Instant exp = Instant.now().plus(jwtProperties.getRefreshTokenTtl());
        return refreshTokenRepository.save(user.getId(), refresh, exp)
                .flatMap(saved -> sessionService.storeSession(user.getId(), refresh, jwtProperties.getRefreshTokenTtl(), deviceId)
                        .thenReturn(new TokenResponse(access, refresh)));
    }
}
