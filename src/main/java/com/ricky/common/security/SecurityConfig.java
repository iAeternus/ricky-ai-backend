package com.ricky.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableReactiveMethodSecurity
@EnableConfigurationProperties({JwtProperties.class, RateLimitProperties.class})
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            AuthenticationWebFilter jwtAuthenticationWebFilter,
            WebFilter rateLimitWebFilter,
            WebFilter xssProtectionWebFilter
    ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers(
                                "/api/swagger/**",
                                "/api/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/api/docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/chat/stream").authenticated()
                        .pathMatchers("/actuator/health").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(rateLimitWebFilter, SecurityWebFiltersOrder.FIRST)
                .addFilterAt(xssProtectionWebFilter, SecurityWebFiltersOrder.FIRST)
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter(
            ReactiveAuthenticationManager authenticationManager,
            ServerAuthenticationConverter authenticationConverter
    ) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(authenticationConverter);
        filter.setRequiresAuthenticationMatcher(jwtMatcher());
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(JwtService jwtService) {
        return new JwtAuthenticationManager(jwtService);
    }

    @Bean
    public ServerAuthenticationConverter serverAuthenticationConverter() {
        return new JwtServerAuthenticationConverter();
    }

    @Bean
    public WebFilter rateLimitWebFilter(
            ReactiveStringRedisTemplate redisTemplate,
            RateLimitProperties properties,
            ObjectMapper objectMapper
    ) {
        return new RateLimitWebFilter(redisTemplate, properties, objectMapper);
    }

    @Bean
    public WebFilter xssProtectionWebFilter() {
        return new XssProtectionWebFilter();
    }

    private org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher jwtMatcher() {
        org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher apiMatcher =
                ServerWebExchangeMatchers.pathMatchers("/api/**");
        org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher swaggerMatcher =
                ServerWebExchangeMatchers.pathMatchers(
                        "/api/swagger/**",
                        "/api/swagger-ui.html",
                        "/api/swagger-ui/**",
                        "/api/docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
        return exchange -> apiMatcher.matches(exchange)
                .flatMap(match -> {
                    if (!match.isMatch()) {
                        return org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch();
                    }
                    return swaggerMatcher.matches(exchange)
                            .flatMap(swMatch -> swMatch.isMatch()
                                    ? org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch()
                                    : org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match());
                });
    }
}
