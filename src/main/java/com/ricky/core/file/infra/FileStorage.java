package com.ricky.core.file.infra;

import reactor.core.publisher.Mono;

public interface FileStorage {
    Mono<String> save(String filename, byte[] content);
}
