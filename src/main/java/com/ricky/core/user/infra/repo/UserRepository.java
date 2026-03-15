package com.ricky.core.user.infra.repo;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.user.domain.User;
import com.ricky.core.user.domain.UserFactory;
import com.ricky.core.user.infra.mapper.UserDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserR2dbcRepository repository;
    private final UserDataMapper mapper;
    private final UserFactory userFactory;

    public Mono<User> save(String email, String passwordHash, String displayName) {
        User user = userFactory.create(email, passwordHash, displayName);
        return repository.save(mapper.toEntity(user)).map(mapper::toAggregateRoot);
    }

    public Mono<User> update(User user) {
        return repository.findById(user.getId())
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(existing -> {
                    mapper.update(existing, user);
                    return repository.save(existing).map(mapper::toAggregateRoot);
                });
    }

    public Mono<User> findById(Long id) {
        return repository.findById(id).map(mapper::toAggregateRoot);
    }

    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toAggregateRoot);
    }

}
