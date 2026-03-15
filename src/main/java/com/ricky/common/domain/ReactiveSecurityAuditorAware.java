package com.ricky.common.domain;

import com.ricky.common.security.SecurityUtils;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveSecurityAuditorAware implements ReactiveAuditorAware<Long> {
    @Override
    public Mono<Long> getCurrentAuditor() {
        return SecurityUtils.currentUserId();
    }
}
